using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class nodeCounter : MonoBehaviour
{
    public int nodeID;
    public TextMeshProUGUI nodeIDText;

    // Start is called before the first frame update
    void Start()
    {
        nodeID = 0;
        foreach (GameObject node in GameObject.FindGameObjectsWithTag("node"))
        {
            nodeID++;
        }
        nodeIDText.text = nodeID.ToString();
    }

    /*
    public int GetNodeID()
    {
        return nodeID;
    }
    */

    // Update is called once per frame
    void Update()
    {

    }
}
