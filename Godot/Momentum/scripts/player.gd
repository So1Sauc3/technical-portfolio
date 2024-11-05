extends CharacterBody3D
@onready var Pincushion: ColorRect = $ShaderLayer/pincushion
@onready var PickupUI: ColorRect = $UILayer/pickupUI
@onready var ItemUI: ColorRect = $UILayer/itemUI
@onready var InvUI: Control = $UILayer/itemUI/inv_UI
@onready var Hotbar: Control = $UILayer/itemUI/hotbar

@onready var Camera: Camera3D = $Camera
@onready var MotionBlur: MeshInstance3D = $Camera/motion_blur

@onready var Body: CollisionShape3D = $Body
@onready var BodyMesh: MeshInstance3D = $BodyMesh

@onready var GroundCheck: RayCast3D = $GroundCheck
@onready var LeftWallCheck: RayCast3D = $LeftWallCheck
@onready var RightWallCheck: RayCast3D = $RightWallCheck

@onready var DashCooldownTimer: Timer = $DashCooldownTimer
@onready var SlideTimer: Timer = $SlideTimer

# Movement
const MAX_VELOCITY_AIR = 5
const MAX_VELOCITY_GROUND = 12.0
const MAX_ACCELERATION = 5 * MAX_VELOCITY_GROUND
const GRAVITY = 24
const STOP_SPEED = 2
const JUMP_IMPULSE = sqrt(3 * GRAVITY * .9)

const MIN_LAUNCH_SPEED = 20
const DASH_SPEED_BOOST = 10
const MAX_AIR_JUMPS = 2
const DEFAULT_FRICTION = 4
const SLIDE_FRICTION = 0
const WALL_CLIMB_SPEED = 5

var friction = DEFAULT_FRICTION
var airFriction = .02
var wallFriction = 2
var direction = Vector3()
var slide_dir = Vector3()
var wish_jump
var wish_accel
var airJumps = MAX_AIR_JUMPS

var CameraRotation = Vector2(0.0,0.0)
var MouseSensitivity = 0.001
var shake_rotation = 0 
var Start_Shake_Rotation = 0

func _ready():
	Global.set_player_reference(self)
	Input.set_mouse_mode(Input.MOUSE_MODE_CAPTURED)

func _input(event):
	if event.is_action_pressed("fullScreen"):
		if DisplayServer.window_get_mode() == DisplayServer.WINDOW_MODE_MAXIMIZED: DisplayServer.window_set_mode(DisplayServer.WINDOW_MODE_WINDOWED)
		else: DisplayServer.window_set_mode(DisplayServer.WINDOW_MODE_MAXIMIZED)
	
	if event.is_action_pressed("ui_cancel"):
		if Input.get_mouse_mode() == Input.MOUSE_MODE_CAPTURED:
			Input.set_mouse_mode(Input.MOUSE_MODE_VISIBLE)
		else:
			Input.set_mouse_mode(Input.MOUSE_MODE_CAPTURED)
		
	if event is InputEventMouseMotion:
		var MouseEvent = event.relative * MouseSensitivity
		CameraLook(MouseEvent)

func CameraLook(Movement: Vector2):
	CameraRotation += Movement
	
	transform.basis = Basis()
	Camera.transform.basis = Basis()
	
	# applying rotations, Y first then X b/c gimbal and yaw/pitch/roll
	rotate_object_local(Vector3(0,1,0),-CameraRotation.x)
	if !is_on_floor() && !GroundCheck.is_colliding() && Input.is_action_pressed("moveAccel"): rotate_object_local(Vector3(1,0,0), -CameraRotation.y)
	else: Camera.rotate_object_local(Vector3(1,0,0), -CameraRotation.y)
	
	# camera correction, keeping rotation values under PI so it doesn't jank if player makes flips > 2*PI
	if CameraRotation.x > PI: CameraRotation.x-=2*PI
	if CameraRotation.x < -PI: CameraRotation.x+=2*PI
	if CameraRotation.y > PI: CameraRotation.y-=2*PI
	if CameraRotation.y < -PI: CameraRotation.y+=2*PI
	if is_on_floor() || !Input.is_action_pressed("moveAccel") || Input.is_action_just_released("moveAccel"): CameraRotation.y = clamp(CameraRotation.y,-PI/2,PI/2)

func _physics_process(delta):
	process_input()
	process_movement(delta)

func process_input():
	# toggle perspective, currently for debug but might add drone later
	if Input.is_action_just_pressed("togglePerspective"):
		if Camera.position.z!=0: Camera.position.z=0
		else: Camera.position.z=3.5
	# toggle hotbar, for no HUD gameplay
	if Input.is_action_just_pressed("toggleUI"):
		ItemUI.visible = !ItemUI.visible
		if Input.get_mouse_mode() == Input.MOUSE_MODE_CAPTURED:
			Input.set_mouse_mode(Input.MOUSE_MODE_VISIBLE)
			InvUI.grab_focus()
		else:
			Input.set_mouse_mode(Input.MOUSE_MODE_CAPTURED)
			InvUI.grab_focus()
	
	# Movement directions and janky camera tilt
	direction = Vector3()
	if Input.is_action_pressed("moveForward"):
		direction -= transform.basis.z
	elif Input.is_action_pressed("moveBackward"):
		direction += transform.basis.z
	if Input.is_action_pressed("moveLeft"):
		direction -= transform.basis.x*2
		Camera.rotation.z = lerp_angle(Camera.rotation.z, deg_to_rad(velocity.length()/2), .05)
	elif Input.is_action_pressed("moveRight"):
		direction += transform.basis.x*2
		Camera.rotation.z = lerp_angle(Camera.rotation.z, deg_to_rad(-velocity.length()/2), .05)
	else:
		Camera.rotation.z = lerp_angle(Camera.rotation.z, deg_to_rad(0), .05)
	
	# Dash / Slide logic
	var blurMat: ShaderMaterial = MotionBlur.get_surface_override_material(0)
	blurMat.set_shader_parameter("intensity", max(DashCooldownTimer.time_left*3-.8,0))
	var pinMat: ShaderMaterial = Pincushion.material
	pinMat.set_shader_parameter("intensity", -1*max(DashCooldownTimer.time_left*3-.7,0))
	if Input.is_action_just_pressed("moveDash") && DashCooldownTimer.time_left==0 && (GroundCheck.is_colliding() || airJumps>0): dash()
	if GroundCheck.is_colliding() || LeftWallCheck.is_colliding() || RightWallCheck.is_colliding(): airJumps = MAX_AIR_JUMPS
	
	
	wish_jump = Input.is_action_pressed("moveJump")
	wish_accel = Input.is_action_pressed("moveAccel")

func process_movement(delta):
	# reset mesh lerp
	if !LeftWallCheck.is_colliding() && !RightWallCheck.is_colliding(): BodyMesh.rotation.z = lerp_angle(BodyMesh.rotation.z, deg_to_rad(0), .05)
	
	# FLOOR LOGIC
	var wish_dir = direction.normalized()
	if is_on_floor():
		# jump
		if wish_jump:
			# launch
			if wish_accel && sqrt(velocity.x*velocity.x+velocity.z+velocity.z)>MIN_LAUNCH_SPEED: 
				velocity.y = JUMP_IMPULSE*3
				wish_accel = false
			else: velocity.y = JUMP_IMPULSE
			
			velocity = update_velocity_air(wish_dir, delta)
			wish_jump = false
		# slide
		elif Input.is_action_pressed("moveSlide"): slide(wish_dir, delta)
		# run
		else:
			stretch(.65, 1.7, .5)
			velocity = update_velocity_ground(wish_dir, delta)
	
	# WALL LOGIC
	elif velocity.length()>MAX_VELOCITY_GROUND/2 && !GroundCheck.is_colliding() && (LeftWallCheck.is_colliding() || RightWallCheck.is_colliding()) && velocity.dot(Camera.get_global_transform().basis.z.normalized())<0:
		# camera tilt control
		if LeftWallCheck.is_colliding():
			BodyMesh.rotation.z = lerp_angle(BodyMesh.rotation.z, deg_to_rad(-30), .05)
			Camera.rotation.z = lerp_angle(Camera.rotation.z, deg_to_rad(-30), .05)
		elif RightWallCheck.is_colliding():
			BodyMesh.rotation.z = lerp_angle(BodyMesh.rotation.z, deg_to_rad(30), .05)
			Camera.rotation.z = lerp_angle(Camera.rotation.z, deg_to_rad(30), .05)
		# wall jump
		if wish_jump:
			# launch
			if wish_accel && sqrt(velocity.x*velocity.x+velocity.z+velocity.z)>MIN_LAUNCH_SPEED: 
				velocity.y = JUMP_IMPULSE*3
				wish_accel = false
			else: velocity.y = JUMP_IMPULSE
			
			velocity+=get_wall_normal()*DASH_SPEED_BOOST
		# wall run
		else:
			if Input.is_action_pressed("moveLeft"):
				if LeftWallCheck.is_colliding(): velocity.y = WALL_CLIMB_SPEED
				elif RightWallCheck.is_colliding(): velocity.y = -WALL_CLIMB_SPEED
			elif Input.is_action_pressed("moveRight"):
				if LeftWallCheck.is_colliding(): velocity.y = -WALL_CLIMB_SPEED
				elif RightWallCheck.is_colliding(): velocity.y = WALL_CLIMB_SPEED
			else: velocity.y = -.5
			velocity-=get_wall_normal()
			velocity = update_velocity_air(wish_dir, delta)
	
	# AIR LOGIC
	else:
		if !Input.is_action_pressed("moveSlide"):
			stretch(.65, 1.7, .5)
		velocity.y -= GRAVITY * delta
		velocity = update_velocity_air(wish_dir, delta)
	
	# Move the player once velocity has been calculated
	move_and_slide()

func update_velocity_ground(wish_dir: Vector3, delta):
	# slow down fast when no inputs
	if wish_dir.length() == 0: return velocity*.6
	
	# Apply friction when on the ground and then accelerate
	var speed = velocity.length()
	if speed != 0: velocity *= max(speed - max(STOP_SPEED, speed)*friction*delta, 0) / speed
	
	return accelerate(wish_dir, MAX_VELOCITY_GROUND, delta)

func update_velocity_air(wish_dir: Vector3, delta):
	# air friction
	var speed = velocity.length()
	if speed != 0: velocity *= max(speed - max(STOP_SPEED, speed)*airFriction*delta, 0) / speed
	
	return accelerate(wish_dir, MAX_VELOCITY_AIR, delta)

func accelerate(wish_dir: Vector3, max_velocity: float, delta):
	# Get our current speed as a projection of velocity onto the wish_dir
	# How much we accelerate is the difference between the max speed and the current speed
	var current_speed = velocity.dot(wish_dir)
	var add_speed = clamp(max_velocity - current_speed, 0, MAX_ACCELERATION * delta)
	
	return velocity + add_speed * wish_dir

func dash():
	DashCooldownTimer.start()
	# directional dashing
	if direction.length()==0 || Input.is_action_pressed("moveForward"):
		velocity = Camera.get_global_transform().basis.z.normalized()*-1*min(velocity.length()+DASH_SPEED_BOOST, MAX_VELOCITY_GROUND*3)
	elif Input.is_action_pressed("moveBackward"):
		velocity = Camera.get_global_transform().basis.z.normalized()*min(velocity.length()+DASH_SPEED_BOOST, MAX_VELOCITY_GROUND*3)
	else: velocity = direction.normalized()*min(velocity.length()+DASH_SPEED_BOOST/2.0, MAX_VELOCITY_GROUND*3)
	# resolve jump count and cooldown
	airJumps-=1

# BROKEN PLEASE FIX
func slide(wish_dir: Vector3, delta):
	if Input.is_action_just_pressed("moveSlide"):
		SlideTimer.start()
		slide_dir = Vector3(1,0,1)*Camera.get_global_transform().basis.z.normalized()*-1
		velocity = slide_dir*MAX_VELOCITY_GROUND*3
	elif SlideTimer.time_left!=0:
		stretch(.2, .8, .5)
		velocity = slide_dir*max(0, velocity.length()-1)
	else:
		stretch(.65, 1.7, .5)
		update_velocity_ground(wish_dir, delta)

func stretch(camHeight: float, height: float, radius: float):
	Camera.transform.origin.y = lerp(Camera.transform.origin.y, camHeight, .5)
	Body.shape.set_height(height)
	Body.shape.set_radius(radius)
	BodyMesh.mesh.set_height(height)
	BodyMesh.mesh.set_radius(radius)
