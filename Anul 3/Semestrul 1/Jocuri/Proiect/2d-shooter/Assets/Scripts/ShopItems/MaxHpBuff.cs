using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//creates a menu in unity editor for making scriptable objects
[CreateAssetMenu(menuName = "ShopItems/MaxHpBuff")]

public class MaxHpBuff : ShopItem
{
    // How many hearts should be added. Set in unity editor.
    public float amount;

    // Maximum number of hearts. Set in unity editor.
    public float maxAmount;

    public override void Apply(GameObject player)
    {
        player.GetComponent<Health>().maxHealth += 2*amount;
        player.GetComponent<Health>().health += 2*amount;
        player.GetComponent<Health>().TakeDamage(0);
    }

    // Check if player max health is less than max amount of hearts
    public override bool CanBePurchased(GameObject player){
        return player.GetComponent<Health>().maxHealth < 2*maxAmount;
    }
}
