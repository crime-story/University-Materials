using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Gives an EnemyBehaviour to an enemy
public class EnemyAI : MonoBehaviour
{
    // EnemyBehaviour set from Unity Editor
    public EnemyBehaviour enemyBehaviour;

    private EnemyBehaviour currentBehaviour;

    // On start init the enemyBehaviour
    void Start()
    {
        SetCurrentEnemyBehaviur(enemyBehaviour);
    }

    public void SetCurrentEnemyBehaviur(EnemyBehaviour behaviour)
    {
        currentBehaviour = Instantiate(behaviour);

        currentBehaviour.Init(this);

        if (currentBehaviour.GetRepetableBehaviourCooldown() > 0)
        {
            InvokeRepeating("InvokeRepetableBehaviour", 0, currentBehaviour.GetRepetableBehaviourCooldown());
        }
    }

    // On fixed update call the enemyBehaviour think method
    void FixedUpdate()
    {
        currentBehaviour.Think(this);
    }

    public void InvokeRepetableBehaviour() {
        currentBehaviour.RepetableBehaviour(this);
    }
}
