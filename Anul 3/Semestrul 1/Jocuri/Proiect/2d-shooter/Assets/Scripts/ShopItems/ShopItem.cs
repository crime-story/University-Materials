using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public abstract class ShopItem : ScriptableObject 
{
    public int cost;

    // variable used to check if the item can be purchased multiple times
    public bool respawnable = false;

    // apply the stat change to the player object
    public abstract void Apply(GameObject gameObject);

    // certain items should not be purchasable in certain situations
    public abstract bool CanBePurchased(GameObject gameObject);
}
