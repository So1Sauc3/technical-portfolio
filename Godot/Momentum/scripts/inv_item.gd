extends Node3D

var scene_path = "res://scenes/inv_item.tscn"
@export var item_type = ""
@export var item_name = ""
@export var item_texture: Texture

@onready var item_mesh = $MeshInstance3D
@onready var in_range = false

func _ready():
	if !Engine.is_editor_hint(): item_mesh.mesh = item_mesh
	pass

func _process(delta):
	if !Engine.is_editor_hint(): item_mesh.mesh = item_mesh
	if in_range && Input.is_action_just_pressed("itemPickup"): pickup_item()

func pickup_item():
	var item = {
		"quantity": 1,
		"type": item_type,
		"name": item_name,
		"texture": item_texture,
		"scene_path": scene_path
	}
	if Global.player_node:
		Global.add(item)
		self.queue_free()

func _on_area_3d_body_entered(body):
	if body.is_in_group("Player"):
		in_range = true
		body.PickupUI.visible = true

func _on_area_3d_body_exited(body):
	if body.is_in_group("Player"):
		in_range = false
		body.PickupUI.visible = false

func set_item_data(data):
	item_type = data["type"]
	item_name = data["name"]
	item_texture = data["texture"]
	scene_path = data["scene_path"]
