using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CoinCollectSound : MonoBehaviour
{
    public AudioSource coinSound;

    // Play the sound when a coin is collected
    public void PlayCoinCollectSound()
    {
        if (!coinSound.isPlaying)
        {
            coinSound.Play();
        }
    }
}
