using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public abstract class Powerup : ScriptableObject 
{
    public abstract void Apply(GameObject gameObject);
}
