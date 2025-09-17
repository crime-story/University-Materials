using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class ScoreManager : MonoBehaviour
{
    public TextMeshProUGUI text;
    int score;

    public int totalGold = 0;
    public TextMeshProUGUI totalGoldText;
    public int levelCleared = 1;
    public TextMeshProUGUI levelClearedText;
    public int enemiesKilled = 0;
    public TextMeshProUGUI enemiesKilledText;

    // Start is called before the first frame update
    void Start()
    {
        
    }
    public int GetScore(){
        return this.score;
    }
    public void ChangeScore(int coinValue)
    {
        totalGold += coinValue;
        totalGoldText.text = "Gold: " + totalGold.ToString();
        score += coinValue;
        text.text = score.ToString();
    }
}
