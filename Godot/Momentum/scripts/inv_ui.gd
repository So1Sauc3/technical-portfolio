extends Control

@onready var grid = $GridContainer

# Called when the node enters the scene tree for the first time.
func _ready():
	Global.inv_update.connect(_on_inventory_updated)
	_on_inventory_updated()

func _on_inventory_updated():
	clear_grid()
	for item in Global.inv:
		var slot = Global.inv_slot_scene.instantiate()
		grid.add_child(slot)
		if item!=null: slot.set_item(item)
		else: slot.set_empty()

func clear_grid():
	while grid.get_child_count()>0:
		var child = grid.get_child(0)
		grid.remove_child(child)
		child.queue_free()
