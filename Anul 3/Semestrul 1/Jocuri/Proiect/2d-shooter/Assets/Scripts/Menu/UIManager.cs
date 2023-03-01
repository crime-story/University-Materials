using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class UIManager : MonoBehaviour
{
    public GameObject gameOverMenu;

    public Health health;
    
    private bool restartPressed = false;

    private void OnEnable()
    {
        health.OnDeath += EnableGameOverMenu;
    }

    private void OnDisable()
    {
        health.OnDeath -= EnableGameOverMenu;
    }

    public void EnableGameOverMenu()
    {
        gameOverMenu.SetActive(true);
    }

    public void DeleteAllGameObjects(){
        GameObject player = GameObject.Find("Player");
        GameObject ui = GameObject.Find("Hearts UI");
        GameObject coins = GameObject.Find("Coin counter");
        GameObject stats = GameObject.Find("PlayerStats");
        Destroy(player);
        Destroy(ui);
        Destroy(coins);
        Destroy(stats);
     }

    public void RestartLevel()
    {
        if(!restartPressed){
            restartPressed = true;
            SceneManager.LoadScene(1);
            Invoke("DeleteAllGameObjects", 0.2f);
        }

    }

    public void GoToMainMenu()
    {
        SceneManager.LoadScene(0);
        Invoke("DeleteAllGameObjects", 0.2f);
    }
}