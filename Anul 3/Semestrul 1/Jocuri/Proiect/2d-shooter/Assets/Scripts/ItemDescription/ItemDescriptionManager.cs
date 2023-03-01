using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class ItemDescriptionManager : MonoBehaviour
{
    
    public TextMeshProUGUI text;
    public float seconds = 7f;
    // Start is called before the first frame update
    void Start()
    {
       
        StartCoroutine(DestroyAfterDelay());
    }

    public void PrintCoinsValue(int coinValue)
    {
        if (coinValue == 1)
        {
            text.text = "+" + coinValue.ToString() + " coin";
        }
        else
        {
            text.text = "+" + coinValue.ToString() + " coins";
        }
        StartCoroutine(DestroyAfterDelay());
    }

    public void PrintGunDescription(string gunName)
    {
        text.text = "You picked up a \"" + gunName + "\" Gun!";
        StartCoroutine(DestroyAfterDelay());
    }

    public void PrintBulletDescription(string bulletName)
    {
        text.text = "You picked up a \"" + bulletName + "\" Bullet!";
        StartCoroutine(DestroyAfterDelay());
    }

    IEnumerator DestroyAfterDelay()
    {
        yield return new WaitForSeconds(seconds);
        text.text = "";
    }
}
