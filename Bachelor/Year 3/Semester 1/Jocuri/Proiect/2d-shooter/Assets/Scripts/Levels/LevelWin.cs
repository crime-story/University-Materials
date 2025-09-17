using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LevelWin : MonoBehaviour
{
    public AudioSource winLevelSound;

    private void Start()
    {
        winLevelSound.Play();
    }
}
