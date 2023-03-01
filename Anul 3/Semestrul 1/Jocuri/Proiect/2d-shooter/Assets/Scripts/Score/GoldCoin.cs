using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GoldCoin : MonoBehaviour
{
    public Rigidbody2D rigidbody;

    void Update()
    {
        rigidbody.AddForce(Vector2.down * 10);
    }
}
