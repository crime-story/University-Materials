using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;


public class GunDescriptionManager : MonoBehaviour
{
    
    public TextMeshProUGUI text;
    public float seconds = 7f;
    // Start is called before the first frame update
    void Start()
    {
        
        StartCoroutine(DestroyAfterDelay());
    }

    public void PrintGunDescription(string gunName)
    {
        if(gunName == "Charged Shot")
            text.text = "Hold down for more damage, release for bigger bullet, 3 charge levels, and 0.75 sec cooldown";
        else if(gunName == "Triple Shot")
            text.text = "3 bullets fired in a cone pattern for increased firepower";
        else if(gunName == "Repeating Shot")
            text.text = "Hold down for more bullets, release for quick burst, 3 charge levels, 0.75 sec cooldown, 0.075 sec delay between bullets";
    
        StartCoroutine(DestroyAfterDelay());
    }

    public void PrintBulletDescription(string bulletName)
    {
        if(bulletName == "Bouncy Shot")
            text.text = "Bullets ricochet off walls, limited bounces, disappears after hitting enemy or after last bounce";
        else if(bulletName =="Homing Shot")
            text.text = "Bullets track nearest enemy, adjusts direction";

        StartCoroutine(DestroyAfterDelay());
    }

    IEnumerator DestroyAfterDelay()
    {
        yield return new WaitForSeconds(seconds);
        text.text = "";
    }
}
