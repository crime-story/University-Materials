using System.Collections;
using System.Collections.Generic;
using UnityEngine;


// EnemyBehaviour that move towards the player following a path
[CreateAssetMenu(menuName = "EnemyBehaviour/PatrolAndActivate")]
public class PatrolAndActivate : EnemyBehaviour
{

    // Tag of the target. When in sight begin activated phase
    public string targetTag = "Player";

    // Patrol enemy behaviour
    public Patrol patrol;

    // Enemy activated behaviour
    public EnemyBehaviour activatedBehaviour;

    // True if enemy is in activated mode
    bool isActivated = false;

    // Animator of the enemy
    Animator animator;

    public override void Init(EnemyAI enemyAI)
    {
        animator = enemyAI.gameObject.GetComponent<Animator>();
        patrol = Instantiate(patrol);
        patrol.Init(enemyAI);
    }

    public override float GetRepetableBehaviourCooldown() 
    {
        return patrol.GetRepetableBehaviourCooldown();
    }

    public override void RepetableBehaviour(EnemyAI enemyAI)
    {
        patrol.RepetableBehaviour(enemyAI);
    }

    public override void Think(EnemyAI enemyAI) 
    {

        patrol.Think(enemyAI);

        // If enemy sees its target start activated mode
        if (!isActivated)
        {
            var target = GameObject.FindGameObjectWithTag(targetTag);
            var enemyTargetLinecast = Physics2D.Linecast(enemyAI.transform.position, target.transform.position, 1 << LayerMask.NameToLayer("Walls"));

            if (!enemyTargetLinecast.collider)
            {
                Debug.Log("Found target. Entered activated phase!");

                // No collision between enemy and target. Start chase phase
                isActivated = true;
                enemyAI.SetCurrentEnemyBehaviur(activatedBehaviour);   

                // Update animation
                if (animator)
                {
                    animator.SetBool("IsInChaseMode", true);
                }

                // Turn on activated light
                var activatedLight =  enemyAI.gameObject.GetComponent<UnityEngine.Rendering.Universal.Light2D>();
                if (activatedLight) {
                    activatedLight.intensity = 0.8f;
                }
             
            }
        }
        
    }

}
