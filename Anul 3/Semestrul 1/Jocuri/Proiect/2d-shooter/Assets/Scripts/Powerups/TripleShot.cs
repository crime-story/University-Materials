using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//creates a menu in unity editor for making scriptable objects
[CreateAssetMenu(menuName = "Powerups/Gun/TripleShot")]

public class TripleShot : Powerup
{
    public override void Apply(GameObject gun)
    {
        gun.GetComponent<GunFire>().AddShots(2);
    }
}
