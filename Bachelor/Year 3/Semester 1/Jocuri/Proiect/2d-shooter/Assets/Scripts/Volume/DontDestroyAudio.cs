using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class DontDestroyAudio : MonoBehaviour
{
    private static DontDestroyAudio instance = null;
    public AudioSource audioSource;

    private void disablePlayerAudioListener() {
        GameObject player = GameObject.FindGameObjectsWithTag("Player")[0];
        player.transform.Find("Main Camera").GetComponent<AudioListener>().enabled = false;
    }

    private void Awake()
    {
        AudioListener[] aL = FindObjectsOfType<AudioListener>();
        if (aL.Length > 1) {
            disablePlayerAudioListener();
        }

        if (instance != null && instance != this)
        {
            Debug.Log("There is already an audio source. Destroy the new one.");
            Destroy(this.gameObject);
            return;
        }

        Debug.Log("An instance of audio source was created.");
        instance = this;
        DontDestroyOnLoad(this.gameObject);
    }

    private void Start()
    {
        audioSource = GetComponent<AudioSource>();
        audioSource.Play();
    }
}
