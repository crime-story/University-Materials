using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class PreviousScore : MonoBehaviour
{
    public TextMeshProUGUI totalGoldText;
    public TextMeshProUGUI levelClearedText;
    public TextMeshProUGUI enemiesKilledText;

    void Start()
    {
        GetPreviousScores();
    }

    public void SetPreviousScores(){
        PlayerPrefs.SetInt("Gold", gameObject.GetComponent<ScoreManager>().totalGold);
        PlayerPrefs.SetInt("Level", gameObject.GetComponent<ScoreManager>().levelCleared);
        PlayerPrefs.SetInt("Enemies", gameObject.GetComponent<ScoreManager>().enemiesKilled);
    }
    public void GetPreviousScores(){
        totalGoldText.text = "Gold: " + PlayerPrefs.GetInt("Gold").ToString();
        levelClearedText.text = "Level: " + PlayerPrefs.GetInt("Level").ToString();
        enemiesKilledText.text = "Enemies: " + PlayerPrefs.GetInt("Enemies").ToString();
    }
}
