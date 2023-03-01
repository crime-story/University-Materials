using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Coin : MonoBehaviour
{
    public int coinValue = 1;

    private void OnTriggerEnter2D(Collider2D other)
    {
        if(other.gameObject.CompareTag("Player"))
        {
            other.gameObject.GetComponent<ScoreManager>().ChangeScore(coinValue);
            other.gameObject.GetComponent<ItemDescriptionManager>().PrintCoinsValue(coinValue);
            
            GameObject level1Coins = GameObject.Find("Level 1 Coins");
            GameObject level2Coins = GameObject.Find("Level 2 Coins");
            GameObject level3Coins = GameObject.Find("Level 3 Coins");

            if (level1Coins != null)
            {
                level1Coins.GetComponent<CoinCollectSound>().PlayCoinCollectSound();
            }
            else if (level2Coins != null)
            {
                level2Coins.GetComponent<CoinCollectSound>().PlayCoinCollectSound();
            }
            else if (level3Coins != null)
            {
                level3Coins.GetComponent<CoinCollectSound>().PlayCoinCollectSound();
            }

            Destroy(gameObject);
        }
    }
}
