using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//creates a menu in unity editor for making scriptable objects
[CreateAssetMenu(menuName = "ShopItems/AttackSpeedBuff")]

public class AttackSpeedBuff : ShopItem
{
    // By how much should the attack speed multiplier(which starts at 1) should be increased. Set in unity editor.
    public float amount;

    // Max value of the attack speed multiplier. Set in unity editor.
    public float maxAmount;

    public override void Apply(GameObject player)
    {
        player.GetComponent<PlayerShooting>().attackSpeedMultiplier += amount;
        player.GetComponent<PlayerShooting>().multiplierText.text = "x" + player.GetComponent<PlayerShooting>().attackSpeedMultiplier.ToString();
        
    }

    // Check if the attack speed multiplier is less than the maximum amount
    public override bool CanBePurchased(GameObject player){
        return player.GetComponent<PlayerShooting>().attackSpeedMultiplier < maxAmount;
    }
}