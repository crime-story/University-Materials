using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEngine;
using TMPro;

public class VolumeManager : MonoBehaviour
{
    [SerializeField] private Slider gameVolumeSlider = null;
    [SerializeField] private TMP_Text gameVolumeTextValue = null;

    [SerializeField] private Slider musicVolumeSlider = null;
    [SerializeField] private TMP_Text musicVolumeTextValue = null;

    [SerializeField] private float defaultGameVolume = 1.0f;
    [SerializeField] private float defaultMusicVolume = 1.0f;

    [SerializeField] private GameObject backgroundMusic;

    private void Start()
    {
        gameVolumeSlider.value = PlayerPrefs.GetFloat("gameVolume", 1);
        gameVolumeTextValue.text = gameVolumeSlider.value.ToString("0.0");
        AudioListener.volume = gameVolumeSlider.value;

        musicVolumeSlider.value = PlayerPrefs.GetFloat("musicVolume", 1);
        musicVolumeTextValue.text = musicVolumeSlider.value.ToString("0.0");
        backgroundMusic.GetComponent<AudioSource>().volume = musicVolumeSlider.value;
    }

    public void GameVolumeSlider(float volume)
    {
        AudioListener.volume = volume;
        gameVolumeTextValue.text = volume.ToString("0.0");
    }

    public void MusicVolumeSlider(float volume)
    {
        backgroundMusic.GetComponent<AudioSource>().volume = volume;
        musicVolumeTextValue.text = volume.ToString("0.0");
        VolumeApply();
    }

    public void VolumeApply()
    {
        PlayerPrefs.SetFloat("musicVolume", backgroundMusic.GetComponent<AudioSource>().volume);
    }

    public void ResetButton(string MenuType)
    {
        if (MenuType == "Audio")
        {
            AudioListener.volume = defaultMusicVolume;
            musicVolumeSlider.value = defaultMusicVolume;
            gameVolumeTextValue.text = defaultMusicVolume.ToString("0.0");
            VolumeApply();
        }
    }
}
