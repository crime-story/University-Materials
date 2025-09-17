using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//creates a menu in unity editor for making scriptable objects
[CreateAssetMenu(menuName = "Powerups/Gun/ChargedShot")]

public class ChargedShot : Powerup
{
    public override void Apply(GameObject gun)
    {
        gun.GetComponent<GunFire>().setChargedShot(true);
    }
}
