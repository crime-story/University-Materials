using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class OnEnterGoToNextScene : MonoBehaviour
{
    private void OnTriggerEnter2D(Collider2D other)
    {
        if(other.gameObject.CompareTag("Player"))
        {
            // increase the number of stages cleared by the player
            if(other.gameObject.GetComponent<ScoreManager>().levelCleared < 3){
                other.gameObject.GetComponent<ScoreManager>().levelCleared += 1;
                other.gameObject.GetComponent<ScoreManager>().levelClearedText.text = "Level: " + other.gameObject.GetComponent<ScoreManager>().levelCleared.ToString();
            }
            // if the player won, set the current scores as previous scores
            else{
                other.gameObject.GetComponent<PreviousScore>().SetPreviousScores();
            }
            
            // move to next level
            SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex + 1);
        }
    }
}
