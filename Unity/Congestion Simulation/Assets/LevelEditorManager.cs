using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Pathfinding;

public class LevelEditorManager : MonoBehaviour
{
    public ItemController[] ItemButtons;
    public GameObject[] ItemPrefabs;
    public int CurrentButtonPressed;
    public GameObject dot;
    public float spawnTimer = 2;

    private void Update()
    {
        // finding cursor
        Vector2 screenPosition = new Vector2(Input.mousePosition.x, Input.mousePosition.y);
        Vector2 worldPosition = Camera.main.ScreenToWorldPoint(screenPosition);

        if(Input.GetMouseButtonDown(1) && ItemButtons[CurrentButtonPressed].Clicked)
        {
            Instantiate(ItemPrefabs[CurrentButtonPressed], new Vector3(
            (float)(Math.Floor(worldPosition.x)+0.5), 
            (float)(Math.Floor(worldPosition.y)+0.5), 
            0), Quaternion.identity);

            AstarPath.active.Scan();
        }

        GameObject[] nodes = GameObject.FindGameObjectsWithTag("node");
        foreach (GameObject node in nodes)
        {
            if (spawnTimer <= 0) {
                Instantiate(dot, new Vector3(node.transform.position.x, node.transform.position.y, 0), Quaternion.identity);
                spawnTimer = 2;
            } else if (spawnTimer > 0)
            {
                spawnTimer -= Time.deltaTime;
            }
        }
    }
}
