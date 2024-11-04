using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ageCounter : MonoBehaviour
{
    public float age = 0;

    // Update is called once per frame
    void Update()
    {
        age += Time.deltaTime;
    }

    public float GetAge()
    {
        return age;
    }
}
