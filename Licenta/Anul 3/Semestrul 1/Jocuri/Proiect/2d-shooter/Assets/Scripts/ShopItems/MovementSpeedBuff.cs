using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

//creates a menu in unity editor for making scriptable objects
[CreateAssetMenu(menuName = "ShopItems/MovementSpeedBuff")]

public class MovementSpeedBuff : ShopItem
{
    // By how much should the movement speed multiplier(which starts at 1) should be increased. Set in unity editor.
    public float amount;

    // Max value of the movement speed multiplier. Set in unity editor.
    public float maxAmount;

    public override void Apply(GameObject player)
    {
        player.GetComponent<PlayerMovement>().movementSpeedMultiplier += amount;
        player.GetComponent<PlayerMovement>().multiplierText.text = "x" + player.GetComponent<PlayerMovement>().movementSpeedMultiplier.ToString();
        
    }

    // Check if the movement speed multiplier is less than the maximum amount
    public override bool CanBePurchased(GameObject player){
        return player.GetComponent<PlayerMovement>().movementSpeedMultiplier < maxAmount;
    }
}
