using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// EnemyBehaviour that move towards the player following a path
[CreateAssetMenu(menuName = "EnemyBehaviour/ChaseAndShoot")]
public class ChaseAndShoot : EnemyBehaviour
{

    // The chase behaviour
    public SmartChase chase;

    // Distance at which to stop and start shooting
    public float shootingRange = 4f;

    // Movement of enemy
    Movement movement;

    // Animator of enemy
    Animator animator;

    // Attack of the enemy
    EnemyRangedAttack attack;

    // If the enemy is attacking
    bool isAttacking = false;

    public override void Init(EnemyAI enemyAI)
    {
        // Instantiate so that multiple enemies don't use the same chase instance
        chase = Instantiate(chase);

        movement = enemyAI.gameObject.GetComponent<Movement>();
        animator = enemyAI.gameObject.GetComponent<Animator>();
        attack = enemyAI.gameObject.GetComponent<EnemyRangedAttack>();
        chase.Init(enemyAI);
    }

    public override float GetRepetableBehaviourCooldown() 
    {
        return chase.GetRepetableBehaviourCooldown();
    }

    public override void RepetableBehaviour(EnemyAI enemyAI)
    {
       chase.RepetableBehaviour(enemyAI);
    }


    public override void Think(EnemyAI enemyAI) 
    {

        // Enemy is attacking. Wait for attack to finish
        if (isAttacking) return;

        var target = GameObject.FindGameObjectWithTag(chase.targetTag);
        var enemyTargetLinecast = Physics2D.Linecast(enemyAI.transform.position, target.transform.position, 1 << LayerMask.NameToLayer("Walls"));
        if (!enemyTargetLinecast.collider &&
            Vector2.Distance(enemyAI.transform.position, target.transform.position) < shootingRange)
        {
            // No collider between enemy and target
            // In shooting range. Start shooting 
            isAttacking = true;              
            movement.Stop();

            // Update animation
            Debug.Log("Start attacking");
            animator.Play("Attack");

            enemyAI.StartCoroutine(WaitForAttackToFinish());

            return;
        }

        // Chase
        chase.Think(enemyAI);
    }


    IEnumerator WaitForAttackToFinish()
    {
        yield return new WaitForSeconds(animator.GetCurrentAnimatorStateInfo(0).length);
        var target = GameObject.FindGameObjectWithTag(chase.targetTag);
        attack.Fire(target.transform.position);

        isAttacking = false;
        animator.Rebind();
        animator.Update(0f);
    }
    
}
