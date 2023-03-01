using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class ApplyItemEffect : MonoBehaviour
{
    // Set in unity editor(using a shopitem scriptable object).
    public ShopItem item;

    // Used for displaying the cost of the item
    public TextMeshPro text;

    void Start()
    {
        text.text = item.cost.ToString();
        
    }

    // When th player touches the item, if the item is not too expensive, apply it's effect and destroy it
    public void OnTriggerEnter2D(Collider2D other){
        if(other.gameObject.tag == "Player" && other.gameObject.GetComponent<ScoreManager>().GetScore() >= item.cost && item.CanBePurchased(other.gameObject)){
            other.gameObject.GetComponent<ScoreManager>().ChangeScore(-item.cost);
            item.Apply(other.gameObject);
            if(item.respawnable){
                // disable the item for a second
                gameObject.SetActive(false);
                Invoke("ReEnable", 1.0f);
            }
            else{
                Destroy(gameObject);
            }
        }
    }

    void ReEnable(){
        gameObject.SetActive(true);
    }

    
}
