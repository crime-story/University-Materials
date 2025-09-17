using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//creates a menu in unity editor for making scriptable objects
[CreateAssetMenu(menuName = "ShopItems/HeartRefill")]

public class HeartRefill : ShopItem
{
    // How many hearts should be restored. Set in unity editor.
    public float amount;

    public override void Apply(GameObject player)
    {
        player.GetComponent<Health>().health = Mathf.Min(player.GetComponent<Health>().health + 2*amount, player.GetComponent<Health>().maxHealth);
        player.GetComponent<Health>().TakeDamage(0);
    }

    // Check if player is not already at max health
    public override bool CanBePurchased(GameObject player){
        return player.GetComponent<Health>().health < player.GetComponent<Health>().maxHealth;
    }
}
