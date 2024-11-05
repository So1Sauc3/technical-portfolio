extends Node

var inv = []
var hotbar = []

const HOTBAR_SIZE = 4

signal inv_update
var player_node: Node = null
@onready var inv_slot_scene = preload("res://scenes/inv_slot.tscn")


func _ready():
	inv.resize(27)
	hotbar.resize(HOTBAR_SIZE)
	pass # Replace with function body.

func add(item):
	for i in range(inv.size()):
		if inv[i]!=null && inv[i]["type"]==item["type"]:
			inv[i]["quantity"]+=item["quantity"]
			inv_update.emit()
			print("Item added", inv)
			return true
		elif inv[i]==null:
			inv[i]=item
			inv_update.emit()
			print("Item added", inv)
			return false
		return false

func add_hotbar(item):
	for i in range(hotbar.size()):
		if hotbar[i]==null:
			hotbar[i] = item
			return true
		return false

func remove(item_type):
	for i in range(inv.size()):
		if inv[i]!=null && inv[i]["type"]==item_type:
			if inv[i]["quantity"]<1: inv[i] = null
			else: inv[i]["quantity"]-=1
			inv_update.emit()
			return true
	return false

func increase_size():
	inv_update.emit()
	
func set_player_reference(player):
	player_node = player

func adjust_drop_position(position: Vector3):
	var radius = 10
	var nearby_items = get_tree().get_nodes_in_group("items")
	for item in nearby_items:
		if item.global_position.distance_to(position)<radius:
			var random_offset = Vector3(randf_range(-radius,radius), 0.0, randf_range(-radius,radius))
			position+=random_offset
			break
	return position

func drop_item(item_data, drop_position):
	var item_scene = load(item_data["scene_path"])
	var item_instance = item_scene.instantiate()
	item_instance.set_item_data(item_data)
	drop_position = adjust_drop_position(drop_position)
	item_instance.global_position = drop_position
	get_tree().current_scene.add_child(item_instance)
