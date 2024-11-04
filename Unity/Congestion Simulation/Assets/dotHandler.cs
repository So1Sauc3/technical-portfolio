using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class dotHandler : MonoBehaviour
{
    public float ageTotal = 0;

    public void OnCollisionEnter2D(Collision2D col)
    {
        if (col.gameObject.tag == "dot")
        {
            ageTotal += col.gameObject.GetComponent<ageCounter>().GetAge();
            Destroy(col.gameObject);
        }
    }

    public float GetAgeTotal()
    {
        return ageTotal;
    }
}
