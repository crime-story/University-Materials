using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class ChestOpener : MonoBehaviour
{
    // Distance from which the player can open and close the chest
    public float interactionDistance = 3f;

    private GameObject Player;

    public GameObject ChestClose, ChestOpen;
    public GameObject[] possibleLoot;

    private bool wasOpened = false;

    public AudioSource chestSound;

    public TextMeshPro chestInstruction;

    // Start is called before the first frame update
    void Start()
    {
        Player = GameObject.FindGameObjectsWithTag("Player")[0];
        ChestClose.SetActive(true);
        ChestOpen.SetActive(false);
        chestInstruction.enabled = false;
    }

    // Update is called once per frame
    void Update()
    {
        Player = GameObject.FindGameObjectsWithTag("Player")[0];

        if (Vector2.Distance(Player.transform.position, transform.position) < interactionDistance)
        {

            if (!wasOpened) {
                chestInstruction.enabled = true;
            } else {
                chestInstruction.enabled = false;
            }

            if (Input.GetKeyDown(KeyCode.E)) 
            {
                if (ChestClose.activeSelf)
                {
                    // open chest
                    ChestClose.SetActive(false);
                    ChestOpen.SetActive(true);

                    // Play the chest sound
                    chestSound.Play();

                    // if it was never opened drop loot
                    if (!wasOpened)
                    {
                        wasOpened = true;
                        DropLoot();
                    }
                }
                else
                {
                    // close chest
                    ChestClose.SetActive(true);
                    ChestOpen.SetActive(false);

                    // Play the chest sound
                    chestSound.Play();
                }
            }
        } else {
            chestInstruction.enabled = false;
        }
    }
    void DropLoot()
    {
        // Randomly select an item from the possible loot
        int randomIndex = Random.Range(0, possibleLoot.Length);

        // Position at the chest
        Vector3 spawnPosition = transform.position;

        if (Player.transform.position.x > transform.position.x)
        {
            spawnPosition.x -= 1f; // move the position to the left of the chest
        }
        else if (Player.transform.position.x < transform.position.x)
        {
            spawnPosition.x += 1f; // move the position to the right of the chest
        }
        else if (Player.transform.position.y > transform.position.y)
        {
            spawnPosition.y += 1f; // move the position above the chest
        }
        else
        {
            spawnPosition.y -= 1f; // move the position below the chest
        }

        spawnPosition.x += Random.Range(-0.85f, 0.85f); // Add a random offset to the x position
        spawnPosition.y += Random.Range(-0.85f, 0.85f); // Add a random offset to the y position

        GameObject droppedItem = Instantiate(possibleLoot[randomIndex], spawnPosition, Quaternion.identity);
    }
}