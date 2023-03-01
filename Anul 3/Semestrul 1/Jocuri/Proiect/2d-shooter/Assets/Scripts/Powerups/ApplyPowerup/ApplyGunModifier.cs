using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ApplyGunModifier : MonoBehaviour
{
    //Set in unity editor(using a powerup scriptable object).
    public Powerup power;

    //when th player touches the item, add the powerup to gun modifiers(list in GunFire.cs)
    public void OnTriggerEnter2D(Collider2D other){
        if(other.gameObject.tag == "Player"){
            other.gameObject.GetComponent<ItemDescriptionManager>().PrintGunDescription(gameObject.tag);
            other.gameObject.GetComponent<GunDescriptionManager>().PrintGunDescription(gameObject.tag);
            power.Apply(other.gameObject.GetComponent<PlayerShooting>().gun.gameObject);
            Destroy(gameObject);
        }
    }
}
