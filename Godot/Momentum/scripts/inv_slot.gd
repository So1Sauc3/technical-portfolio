extends Control

@onready var icon = $in_border/ItemIcon
@onready var quantity_label = $in_border/ItemQuantity

@onready var details_panel = $DetailsPanel
@onready var item_name = $DetailsPanel/ItemName
@onready var item_type = $DetailsPanel/ItemType
@onready var item_effect = $DetailsPanel/ItemEffect

@onready var usage_panel =$UsagePanel

# Slot item
var item = null

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass

func _on_item_button_pressed():
	print("pressed")
	if item!=null: usage_panel.visible = !usage_panel.visible

func _on_item_button_mouse_entered():
	print("entered")
	if item!=null:
		usage_panel.visible = false
		details_panel.visible = true

func _on_item_button_mouse_exited():
	print("exited")
	details_panel.visible = false

func set_empty():
	icon.texture = null
	quantity_label.text = ""

func set_item(new_item):
	item = new_item
	icon.texture = new_item["texture"]
	quantity_label.text = str(item["quantity"])

