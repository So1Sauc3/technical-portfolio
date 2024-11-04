using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class endnodeCounter : MonoBehaviour
{
    public int endnodeID;
    public TextMeshProUGUI endnodeIDText;

    // Start is called before the first frame update
    void Start()
    {
        endnodeID = 0;
        foreach (GameObject endnode in GameObject.FindGameObjectsWithTag("endnode"))
        {
            endnodeID++;
        }
        endnodeIDText.text = endnodeID.ToString();

    }

    /*
    public int GetEndNodeID()
    {
        return endnodeID;
    }
    */
}
