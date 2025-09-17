using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

public class Health : MonoBehaviour
{
    public event Action OnDamaged;
    public event Action OnDeath;

    public float health, maxHealth;

    public bool godMode = false;

    // Sound when player is die
    public AudioSource playerDeathSound;

    // Sound when player takes damage
    public AudioSource playerDamageSound;

    void Start()
    {
        health = maxHealth;
    }

    void Update()
    {
        if (Input.GetKeyDown(KeyCode.F1))
        {
            godMode = !godMode;
        }
    }

    public void TakeDamage(float amount)
    {
        if (!godMode || this.gameObject.tag != "Player")
        {
            health -= amount;
            OnDamaged?.Invoke();

            if (this.gameObject.tag == "Player" && amount > 0)
            {
                playerDamageSound.Play();
            }

            if (health <= 0)
            {
                // if an enemy dies, increase the number of enemies killed by the player
                if(this.gameObject.tag == "Enemy"){
                    GameObject player = GameObject.FindGameObjectWithTag("Player");
                    // if the player is dead and an enemy dies after him, the score shoul not increase
                    if(player.GetComponent<Health>().health > 0){
                        player.GetComponent<ScoreManager>().enemiesKilled += 1;
                        player.GetComponent<ScoreManager>().enemiesKilledText.text = "Enemies: " + player.GetComponent<ScoreManager>().enemiesKilled.ToString();
                    }
                }

                // if the player dies, save the number of levels completed, enemies killed etc.
                if (this.gameObject.tag == "Player"){
                    this.gameObject.GetComponent<PreviousScore>().SetPreviousScores();
                }

                if (this.gameObject.tag == "Player" && health == 0)
                {
                    playerDeathSound.Play();
                }
                else if (this.gameObject.tag == "Enemy" && health == 0)
                {
                    GameObject level1Enemies = GameObject.Find("Level 1 Enemies");
                    GameObject level2Enemies = GameObject.Find("Level 2 Enemies");
                    GameObject level3Enemies = GameObject.Find("Level 3 Enemies");

                    if (level1Enemies != null)
                    {
                        level1Enemies.GetComponent<KillEnemySound>().PlayEnemyDeathsSound();
                    }
                    else if (level2Enemies != null)
                    {
                        level2Enemies.GetComponent<KillEnemySound>().PlayEnemyDeathsSound();
                    }
                    else if (level3Enemies != null)
                    {
                        level3Enemies.GetComponent<KillEnemySound>().PlayEnemyDeathsSound();
                    }
                }
                health = 0;
                Debug.Log("You're dead");
                OnDeath?.Invoke();
            }
        }
    }
}
