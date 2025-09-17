using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class KillEnemySound : MonoBehaviour
{
    public AudioSource enemyDeathSound;

    // Play the sound when enemy dies
    public void PlayEnemyDeathsSound()
    {
        if (!enemyDeathSound.isPlaying)
        {
            enemyDeathSound.Play();
        }
    }
}
