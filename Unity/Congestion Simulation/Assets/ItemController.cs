using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ItemController : MonoBehaviour
{
    public int ID;
    public bool Clicked = false;
    private LevelEditorManager editor;

    void Start()
    {
        editor = GameObject.FindGameObjectWithTag("LevelEditorManager").GetComponent<LevelEditorManager>();
    }

    public void ButtonClicked()
    {
        Clicked = true;
        editor.CurrentButtonPressed = ID;
    }
}
