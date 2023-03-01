using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

public class PlayerMovement : MonoBehaviour
{

    // Speed of player. Set in Unity Editor
    public float moveSpeed = 5;

    // Multiplier increased by shop items. As it increases, the player should move faster.
    [HideInInspector] public float movementSpeedMultiplier = 1f;

    // Ui text used for showing the movementSpeedMultiplier
    public TextMeshProUGUI multiplierText;

    // RigidBody of player. Set in Unity Editor
    public Rigidbody2D rigidbody;

    // Animator used to set the current animation. Set in Unity Editor
    public Animator animator;

    // Referance to Health script
    public Health health;

    // Sprite renderer used to flip the player based on horizontal movement. Set in Unity Editor
    public SpriteRenderer spriteRenderer;

    // Direction of the player. Set by user inputs
    private Vector2 moveDirection;

    // The speed before using god mode
    public float baseSpeed;

    // Maximum speed for player using god mode
    public float maxSpeed = 12f;

    // Increasing speed for god mode
    public bool godModeSpeed = false;

    // God mode sound effect
    [SerializeField] private AudioSource godModeSound;

    // Message for God Mode enabled / disabled
    public TMPro.TextMeshProUGUI godModeText = null;

    [SerializeField] private AudioSource walkSoundEffect;

    private void OnEnable()
    {
        health.OnDeath += DisablePlayerMovement;
    }

    private void OnDisable()
    {
        health.OnDeath -= DisablePlayerMovement;
    }

    void hideText()
    {
        godModeText.gameObject.SetActive(false);
    }

    // Start is called before the first frame update
    void Start()
    {
        multiplierText.text = "x" + movementSpeedMultiplier.ToString();
        EnablePlayerMovement();
        baseSpeed = moveSpeed;
        godModeText.gameObject.SetActive(false);
    }
    // Update is called once per frame
    void Update()
    {
        updateMoveDirectionFromInputs();
        if (Input.GetKeyDown(KeyCode.F1))
        {
            godModeSpeed = !godModeSpeed;
            godModeText.gameObject.SetActive(godModeSpeed);

            if (godModeSpeed)
            {
                godModeText.gameObject.SetActive(true);
                godModeText.text = "God mode enabled!";
                godModeSound.Play(); // play the god mode sound effect
                Invoke("hideText", 2);
            }
            else
            {
                godModeText.gameObject.SetActive(true);
                godModeText.text = "God mode disabled!";
                Invoke("hideText", 2);
            }
        }
        if (godModeSpeed)
        {
            moveSpeed = Mathf.Clamp(moveSpeed + 0.5f, 0f, maxSpeed);
        }
        else
        {
            moveSpeed = Mathf.Clamp(baseSpeed, 0f, maxSpeed);
        }
    }

    // Independent of framerate
    void FixedUpdate()
    {
        MovePlayer();
    }

    // Get moveDirection from inputs
    void updateMoveDirectionFromInputs()
    {
        float moveX = Input.GetAxisRaw("Horizontal");
        float moveY = Input.GetAxisRaw("Vertical");
        moveDirection = new Vector2(moveX, moveY).normalized;

        // Update variable for animator to switch between idle and running.
        animator.SetFloat("Speed", Mathf.Abs(moveX) + Mathf.Abs(moveY));

        // Flip player based on horizontal movement
        if (moveX > 0)
        {
            // normal
            spriteRenderer.flipX = false;
        }
        else if (moveX < 0)
        {
            // moving to the left -> flip
            spriteRenderer.flipX = true;
        }
        if (Mathf.Abs(moveX) > 0 || Mathf.Abs(moveY) > 0)
        {
            if (!walkSoundEffect.isPlaying)
            {
                walkSoundEffect.Play();
            }
        } else {
            if (walkSoundEffect.isPlaying)
            {
                walkSoundEffect.Stop();
            }
        }
    }

    // Move the player based on the direction and speed
    void MovePlayer() 
    {
        rigidbody.velocity = new Vector2(moveDirection.x * (movementSpeedMultiplier * moveSpeed), moveDirection.y * (movementSpeedMultiplier * moveSpeed));
    }

    private void DisablePlayerMovement()
    {
        animator.enabled = false;
        rigidbody.bodyType = RigidbodyType2D.Static;
    }

    private void EnablePlayerMovement()
    {
        animator.enabled = true;
        rigidbody.bodyType = RigidbodyType2D.Dynamic;
    }
}