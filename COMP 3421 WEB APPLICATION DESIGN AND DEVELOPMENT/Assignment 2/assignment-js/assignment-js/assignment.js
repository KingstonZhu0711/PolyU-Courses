/**
 * Assignment 2: JavaScript Fundamentals - Independent Questions
 * 
 * Instructions:
 * - Complete all TODO sections below
 * - Each question is INDEPENDENT (not connected to others)
 * - Use prototypes (NOT ES6 classes) for object construction
 * - Use closures for private state management
 * - Throw errors for invalid inputs (do NOT use default values)
 * - Follow the function signatures exactly as specified
 */

// ============================================
// QUESTION 1: CLOSURES - BANK ACCOUNT SYSTEM
// ============================================

/**
 * Creates a bank account with private balance and transaction history
 * @param {number} initialBalance - Starting balance (must be >= 0)
 * @param {string} accountHolder - Name of account holder (non-empty string)
 * @returns {Object} Account object with methods: deposit, withdraw, getBalance, getAccountHolder, getTransactionHistory
 */
function createBankAccount(initialBalance, accountHolder) {
  // TODO: Implement closure-based bank account
  // - Validate initialBalance (must be number >= 0)
  // - Validate accountHolder (must be non-empty string)
  // - Create private variables: balance, holder, transactions array
  // - Return object with five methods:
  //   * deposit(amount): adds to balance, records transaction
  //   * withdraw(amount): removes from balance, records transaction, checks sufficient funds
  //   * getBalance(): returns current balance
  //   * getAccountHolder(): returns account holder name
  //   * getTransactionHistory(): returns copy of transactions array
  //
  // Transaction format: {type: 'deposit'|'withdraw', amount: number, balance: number, timestamp: ISO string}
  //
  // Hints:
  // - Use Date() for timestamps: new Date().toISOString()
  // - Return array copy: [...transactions]
  // - Validate amount > 0 for deposits and withdrawals

  if (typeof initialBalance !== 'number' || isNaN(initialBalance) || !isFinite(initialBalance)) {
    throw new Error('Initial balance must be a valid finite number');
  }
  if (initialBalance < 0) {
    throw new Error('Initial balance must be a number >= 0');
  }

  if (typeof accountHolder !== 'string') {
    throw new Error('Account holder must be a string');
  }
  const trimmedHolder = accountHolder.trim();
  if (trimmedHolder === '') {
    throw new Error('Account holder cannot be an empty string');
  }

  let balance = initialBalance;
  const holder = trimmedHolder;
  const transactions = [];

  function validateTransactionAmount(amount, transactionType) {
    if (typeof amount !== 'number' || isNaN(amount) || !isFinite(amount)) {
      throw new Error(`${transactionType.charAt(0).toUpperCase() + transactionType.slice(1)} amount must be a valid finite number`);
    }
    if (amount <= 0) {
      throw new Error(`${transactionType.charAt(0).toUpperCase() + transactionType.slice(1)} amount must be a positive number`);
    }
  }

  return {
    deposit: function (amount) {
      validateTransactionAmount(amount, 'deposit');
      balance += amount;
      const transaction = {
        type: 'deposit',
        amount: amount,
        balance: balance,
        timestamp: new Date().toISOString()
      };
      transactions.push(transaction);
    },

    withdraw: function (amount) {
      validateTransactionAmount(amount, 'withdraw');
      if (balance < amount) {
        throw new Error('Current account deposit does not support withdraw amount ');
      }
      balance -= amount;
      const transaction = {
        type: 'withdraw',
        amount: amount,
        balance: balance,
        timestamp: new Date().toISOString()
      };
      transactions.push(transaction);
    },

    getBalance: function () {
      return balance;
    },

    getAccountHolder: function () {
      return holder;
    },

    getTransactionHistory: function () {
      return [...transactions];
    }
  }

  // throw new Error("Question 1 not yet implemented");
}


// ============================================
// QUESTION 2: PROTOTYPES - GEOMETRIC SHAPES
// ============================================

/**
 * Circle constructor function
 * @param {number} x - Center x coordinate
 * @param {number} y - Center y coordinate
 * @param {number} radius - Circle radius (must be positive)
 * @param {string} color - Fill color
 */
function Circle(x, y, radius, color) {
  // TODO: Implement Circle constructor
  // - Validate all inputs (x, y, radius must be numbers, radius > 0, color is string)
  // - Set properties: this.x, this.y, this.radius, this.color
  //
  // Hint: throw new Error('...') for invalid inputs

  if (typeof x !== 'number' || isNaN(x) || !isFinite(x)) {
    throw new Error('x coordinate must be a valid finite number');
  }
  if (typeof y !== 'number' || isNaN(y) || !isFinite(y)) {
    throw new Error('y coordinate must be a valid finite number');
  }
  if (typeof radius !== 'number' || isNaN(radius) || !isFinite(radius)) {
    throw new Error('Radius must be a valid finite number');
  }
  if (radius <= 0) {
    throw new Error('Radius must be positive');
  }
  if (typeof color !== 'string') {
    throw new Error('Color must be a string');
  }
  this.x = x;
  this.y = y;
  this.radius = radius;
  this.color = color;

  // throw new Error("Question 2: Circle not yet implemented");
}

/**
 * Returns the area of the circle
 * @returns {number} Area (π × radius²)
 */
Circle.prototype.getArea = function () {
  // TODO: Implement getArea on Circle prototype
  // Formula: Math.PI * radius * radius

  return Math.PI * this.radius * this.radius;

  // throw new Error("Circle.getArea not yet implemented");
};

/**
 * Returns the perimeter of the circle
 * @returns {number} Perimeter (2 × π × radius)
 */
Circle.prototype.getPerimeter = function () {
  // TODO: Implement getPerimeter on Circle prototype
  // Formula: 2 * Math.PI * radius

  return 2 * Math.PI * this.radius;

  // throw new Error("Circle.getPerimeter not yet implemented");
};

/**
 * Draws the circle on canvas
 * @param {CanvasRenderingContext2D} ctx - Canvas context
 */
Circle.prototype.draw = function (ctx) {
  // TODO: Implement draw on Circle prototype
  // - Set ctx.fillStyle to this.color
  // - Use ctx.beginPath(), ctx.arc(x, y, radius, 0, Math.PI * 2), ctx.fill()

  ctx.fillStyle = this.color;
  ctx.beginPath();
  ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2);
  ctx.fill();

  // throw new Error("Circle.draw not yet implemented");
};

/**
 * Checks if a point is inside the circle
 * @param {number} px - Point x coordinate
 * @param {number} py - Point y coordinate
 * @returns {boolean} True if point is inside circle
 */
Circle.prototype.contains = function (px, py) {
  // TODO: Implement contains on Circle prototype
  // - Calculate distance from (px, py) to (this.x, this.y)
  // - Distance formula: sqrt((px - x)² + (py - y)²)
  // - Return true if distance <= radius

  const dx = px - this.x;
  const dy = py - this.y;
  const distance = dx * dx + dy * dy;
  const radius = this.radius * this.radius;
  return distance <= radius;

  // throw new Error("Circle.contains not yet implemented");
};

/**
 * Rectangle constructor function
 * @param {number} x - Top-left x coordinate
 * @param {number} y - Top-left y coordinate
 * @param {number} width - Rectangle width (must be positive)
 * @param {number} height - Rectangle height (must be positive)
 * @param {string} color - Fill color
 */
function Rectangle(x, y, width, height, color) {
  // TODO: Implement Rectangle constructor
  // - Validate all inputs
  // - Set properties: this.x, this.y, this.width, this.height, this.color
  if (typeof x !== 'number' || isNaN(x) || !isFinite(x)) {
    throw new Error('x coordinate must be a valid finite number');
  }
  if (typeof y !== 'number' || isNaN(y) || !isFinite(y)) {
    throw new Error('y coordinate must be a valid finite number');
  }
  if (typeof width !== 'number' || isNaN(width) || !isFinite(width)) {
    throw new Error('Width must be a valid finite number');
  }
  if (width <= 0) {
    throw new Error('Width must be positive');
  }
  if (typeof height !== 'number' || isNaN(height) || !isFinite(height)) {
    throw new Error('Height must be a valid finite number');
  }
  if (height <= 0) {
    throw new Error('Height must be positive');
  }
  if (typeof color !== 'string') {
    throw new Error('Color must be a string');
  }
  this.x = x;
  this.y = y;
  this.width = width;
  this.height = height;
  this.color = color;

  // throw new Error("Question 2: Rectangle not yet implemented");
}

/**
 * Returns the area of the rectangle
 * @returns {number} Area (width × height)
 */
Rectangle.prototype.getArea = function () {
  // TODO: Implement getArea on Rectangle prototype
  // Formula: width * height

  return this.width * this.height;

  // throw new Error("Rectangle.getArea not yet implemented");
};

/**
 * Returns the perimeter of the rectangle
 * @returns {number} Perimeter (2 × (width + height))
 */
Rectangle.prototype.getPerimeter = function () {
  // TODO: Implement getPerimeter on Rectangle prototype
  // Formula: 2 * (width + height)

  return 2 * (this.width + this.height);

  // throw new Error("Rectangle.getPerimeter not yet implemented");
};

/**
 * Draws the rectangle on canvas
 * @param {CanvasRenderingContext2D} ctx - Canvas context
 */
Rectangle.prototype.draw = function (ctx) {
  // TODO: Implement draw on Rectangle prototype
  // - Set ctx.fillStyle to this.color
  // - Use ctx.fillRect(x, y, width, height)

  ctx.fillStyle = this.color;
  ctx.fillRect(this.x, this.y, this.width, this.height);

  // throw new Error("Rectangle.draw not yet implemented");
};

/**
 * Checks if a point is inside the rectangle
 * @param {number} px - Point x coordinate
 * @param {number} py - Point y coordinate
 * @returns {boolean} True if point is inside rectangle
 */
Rectangle.prototype.contains = function (px, py) {
  // TODO: Implement contains on Rectangle prototype
  // Return true if:
  // - px >= this.x AND px <= this.x + this.width
  // - py >= this.y AND py <= this.y + this.height

  return px >= this.x && px <= this.x + this.width &&
    py >= this.y && py <= this.y + this.height;

  // throw new Error("Rectangle.contains not yet implemented");
};


// ============================================
// QUESTION 3: ADVANCED CLOSURES - STOPWATCH
// ============================================

/**
 * Creates a stopwatch with lap timing functionality
 * @returns {Object} Stopwatch object with methods: start, stop, reset, lap, getTime, getLaps
 */
function createStopwatch() {
  // TODO: Implement closure-based stopwatch
  // - Create private variables:
  //   * startTime: when stopwatch was started (Date.now())
  //   * elapsedTime: accumulated milliseconds
  //   * intervalId: setInterval ID
  //   * isRunning: boolean status
  //   * laps: array of lap times
  //
  // - Return object with six methods:
  //   * start(): starts timer (throw error if already running)
  //   * stop(): pauses timer, returns elapsed time
  //   * reset(): resets to 0 (throw error if running)
  //   * lap(): records current time (throw error if not running)
  //   * getTime(): returns current elapsed time
  //   * getLaps(): returns copy of laps array
  //
  // Hints:
  // - Use setInterval to update elapsed time every 10ms
  // - Clear interval with clearInterval(intervalId)
  // - Time = Date.now() - startTime + previousElapsedTime

  let startTime = null;
  let elapsedTime = 0;
  let intervalId = null;
  let isRunning = false;
  let laps = [];

  return {
    start: function () {
      if (isRunning) throw new Error('Stopwatch is already running');
      isRunning = true;
      const previousElapsed = elapsedTime;
      startTime = Date.now();
      intervalId = setInterval(() => {
        elapsedTime = Date.now() - startTime + previousElapsed;
      }, 10);
    },
    stop: function () {
      if (!isRunning) return elapsedTime;
      clearInterval(intervalId);
      intervalId = null;
      isRunning = false;
      return elapsedTime;
    },
    reset: function () {
      if (isRunning) throw new Error('Running stopwatch can not be reset');
      elapsedTime = 0;
      laps = [];
      startTime = null;
    },
    lap: function () {
      if (!isRunning) throw new Error('Stopwatch must be running to record a lap');
      laps.push(this.getTime());
    },
    getTime: function () { return elapsedTime; },
    getLaps: function () { return [...laps]; }
  };

  // throw new Error("Question 3 not yet implemented");
}


// ============================================
// QUESTION 4: COMPREHENSIVE - BOUNCING BALLS
// ============================================

/**
 * Ball constructor function for physics simulation
 * @param {number} x - Initial x position
 * @param {number} y - Initial y position
 * @param {number} radius - Ball radius (must be positive)
 * @param {string} color - Ball color
 * @param {number} vx - Velocity x (optional, random if not provided)
 * @param {number} vy - Velocity y (optional, random if not provided)
 */
function Ball(x, y, radius, color, vx, vy) {
  // TODO: Implement Ball constructor
  // - Validate required parameters (x, y, radius, color)
  // - Set properties: x, y, radius, color
  // - Set vx, vy: use provided values OR random values between -3 and 3
  // - Store originalColor for collision detection
  //
  // Hint: Random velocity: Math.random() * 6 - 3
  const validateNumber = (value, name) => {
    if (typeof value !== 'number' || isNaN(value) || !isFinite(value)) {
      throw new Error(`${name} must be a valid finite number`);
    }
  };
  validateNumber(x, 'X coordinate');
  validateNumber(y, 'Y coordinate');
  validateNumber(radius, 'Radius');

  if (radius <= 0) throw new Error('Radius must be a positive number');
  if (typeof color !== 'string') throw new Error('Color must be a string');

  this.x = x;
  this.y = y;
  this.radius = radius;
  this.color = color;
  this.originalColor = color;

  this.vx = typeof vx === 'number' ? vx : Math.random() * 6 - 3;
  this.vy = typeof vy === 'number' ? vy : Math.random() * 6 - 3;

  // throw new Error("Question 4: Ball constructor not yet implemented");
}

/**
 * Updates ball position and handles wall bouncing
 * @param {number} canvasWidth - Canvas width for boundary checking
 * @param {number} canvasHeight - Canvas height for boundary checking
 * @param {boolean} gravity - Whether to apply gravity
 */
Ball.prototype.update = function (canvasWidth, canvasHeight, gravity) {
  // TODO: Implement Ball update method
  // 1. Move ball: this.x += this.vx; this.y += this.vy;
  // 2. Apply gravity if enabled: this.vy += 0.2;
  // 3. Check wall collisions:
  //    - Left/right walls: if (this.x - radius < 0 || this.x + radius > canvasWidth) { this.vx *= -1; }
  //    - Top/bottom walls: if (this.y - radius < 0 || this.y + radius > canvasHeight) { this.vy *= -0.9; }
  // 4. Constrain position: keep ball inside canvas bounds
  //
  // Hint: Math.max(min, Math.min(max, value)) constrains value
  if (gravity) this.vy += 0.2;

  this.x += this.vx;
  this.y += this.vy;

  if (this.x - this.radius < 0) {
    this.x = this.radius; // Constrain to canvas
    this.vx *= -1;
  } else if (this.x + this.radius > canvasWidth) {
    this.x = canvasWidth - this.radius; // Constrain to canvas
    this.vx *= -1;
  }

  if (this.y - this.radius < 0) {
    this.y = this.radius; // Constrain to canvas
    this.vy *= -0.9;
  } else if (this.y + this.radius > canvasHeight) {
    this.y = canvasHeight - this.radius; // Constrain to canvas
    this.vy *= -0.9;
  }

  this.x = Math.max(this.radius, Math.min(canvasWidth - this.radius, this.x));
  this.y = Math.max(this.radius, Math.min(canvasHeight - this.radius, this.y));

  // throw new Error("Ball.update not yet implemented");
};

/**
 * Draws the ball on canvas
 * @param {CanvasRenderingContext2D} ctx - Canvas context
 */
Ball.prototype.draw = function (ctx) {
  // TODO: Implement Ball draw method
  // - Set ctx.fillStyle to this.color
  // - Draw circle: ctx.beginPath(), ctx.arc(x, y, radius, 0, Math.PI * 2), ctx.fill()

  ctx.fillStyle = this.color;
  ctx.beginPath();
  ctx.arc(this.x, this.y, this.radius, 0, Math.PI * 2);
  ctx.fill();
  ctx.closePath();

  // throw new Error("Ball.draw not yet implemented");
};

/**
 * Checks collision with another ball
 * @param {Ball} otherBall - Another ball to check collision with
 * @returns {boolean} True if balls are colliding
 */
Ball.prototype.collidesWith = function (otherBall) {
  // TODO: Implement collision detection
  // 1. Calculate dx = this.x - otherBall.x
  // 2. Calculate dy = this.y - otherBall.y
  // 3. Calculate distance = Math.sqrt(dx * dx + dy * dy)
  // 4. Return true if distance < this.radius + otherBall.radius

  if (!(otherBall instanceof Ball)) return false;
  const dx = this.x - otherBall.x;
  const dy = this.y - otherBall.y;
  const distanceSquared = dx * dx + dy * dy;
  const combinedRadius = this.radius + otherBall.radius;
  return distanceSquared < combinedRadius * combinedRadius;

  // throw new Error("Ball.collidesWith not yet implemented");
};

/**
 * Creates a ball physics simulation system
 * @param {HTMLCanvasElement} canvas - Canvas element for animation
 * @returns {Object} Simulation controller with methods
 */
function createBallSimulation(canvas) {
  // TODO: Implement closure-based ball simulation
  // - Validate canvas (instanceof HTMLCanvasElement)
  // - Create private variables:
  //   * balls: array of Ball objects
  //   * ctx: canvas.getContext('2d')
  //   * animationId: requestAnimationFrame ID
  //   * gravityEnabled: boolean
  //
  // - Create animate() function that:
  //   1. Clears canvas
  //   2. Updates all balls
  //   3. Checks collisions (nested loop)
  //   4. Draws all balls
  //   5. Calls requestAnimationFrame(animate)
  //
  // - Return object with methods:
  //   * addBall(x, y, radius, color): creates and adds new Ball
  //   * start(): begins animation loop
  //   * stop(): cancels animation
  //   * clear(): removes all balls
  //   * setGravity(enabled): toggles gravity
  //   * handleClick(event): adds ball at click position
  //
  // Hints:
  // - Collision detection: for (i=0; i<balls.length; i++) for (j=i+1; j<balls.length; j++)
  // - Clear canvas: ctx.clearRect(0, 0, canvas.width, canvas.height)
  // - Cancel animation: cancelAnimationFrame(animationId)

  if (!(canvas instanceof HTMLCanvasElement)) {
    throw new Error('Argument must be an HTMLCanvasElement');
  }

  const ctx = canvas.getContext('2d');
  let balls = [];
  let animationId = null;
  let gravityEnabled = false;

  function animate() {
    ctx.fillStyle = 'rgba(20, 20, 35, 0.07)'; 
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    balls.forEach(ball => {
      ball.update(canvas.width, canvas.height, gravityEnabled);
      ball.color = ball.originalColor;
    });

    for (let i = 0; i < balls.length; i++) {
      for (let j = i + 1; j < balls.length; j++) {
        const ballA = balls[i];
        const ballB = balls[j];
        if (ballA.collidesWith(ballB)) {
          ballA.color = 'red';
          ballB.color = 'red';
          console.log(`Collision detected: Ball ${i} and Ball ${j}`);
        }
      }
    }

    balls.forEach(ball => ball.draw(ctx));

    animationId = requestAnimationFrame(animate);
  }

  return {
    addBall: function (x, y, radius = 20, color) {
      const randomColor = `#${Math.floor(Math.random() * 16777215).toString(16)}`;
      const ballColor = color || randomColor;
      const ball = new Ball(x, y, radius, ballColor);
      balls.push(ball);
      console.log(`Added new ball: Total balls = ${balls.length}`);
      return ball;
    },
    start: function () {
      if (!animationId) {
        animationId = requestAnimationFrame(animate);
        console.log('Simulation started');
      }
    },
    stop: function () {
      if (animationId) {
        cancelAnimationFrame(animationId);
        animationId = null;
        console.log('Simulation stopped');
      }
    },
    clear: function () {
      balls = [];
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      console.log('All balls cleared');
    },
    setGravity: function (enabled) {
      gravityEnabled = Boolean(enabled);
      console.log(`Gravity ${gravityEnabled ? 'enabled' : 'disabled'}`);
    },
    handleClick: function (event) {
      const rect = canvas.getBoundingClientRect();
      const clickX = event.clientX - rect.left;
      const clickY = event.clientY - rect.top;
      const randomRadius = Math.floor(Math.random() * 20) + 10;
      this.addBall(clickX, clickY, randomRadius);
    }
  }

  // throw new Error("Question 4: createBallSimulation not yet implemented");
}


// ============================================
// QUESTION 5: FIREWORKS ANIMATION SYSTEM
// ============================================

/**
 * Particle constructor for fireworks explosions (Note: Different from Q2!)
 * @param {number} x - Initial x position
 * @param {number} y - Initial y position
 * @param {string} color - Particle color
 * @param {number} size - Particle size (radius)
 */
function ParticleFirework(x, y, color, size) {
  // TODO: Implement Particle constructor for fireworks
  // - Validate all inputs (x, y, size must be numbers; color must be string)
  // - Set properties: x, y, color, size
  // - Set vx (velocity x) to random value between -2 and 2
  // - Set vy (velocity y) to random value between -2 and 2
  // - Set life to 1.0 (will decrease over time for fade out effect)
  //
  // Hint: Random velocity: Math.random() * 4 - 2

  if (typeof x !== 'number' || typeof y !== 'number' || typeof size !== 'number') {
    throw new TypeError('x, y, size must be numbers');
  }
  if (typeof color !== 'string') {
    throw new TypeError('color must be a string');
  }
  this.x = x;
  this.y = y;
  this.color = color;
  this.size = size;
  this.vx = Math.random() * 4 - 2;
  this.vy = Math.random() * 4 - 2;
  this.life = 1.0;
  // throw new Error("Question 5: ParticleFirework not yet implemented");
}

/**
 * Updates particle position, applies physics, and decreases life
 */
ParticleFirework.prototype.update = function () {
  // TODO: Implement update method on prototype
  // - Update position: x += vx, y += vy
  // - Apply gravity: vy += 0.1 (makes particles fall)
  // - Decrease life by 0.01 (particles fade out over time)

  this.x += this.vx;
  this.y += this.vy;
  this.vy += 0.1;
  this.life -= 0.01;

  // throw new Error("ParticleFirework.update not yet implemented");
};

/**
 * Draws the particle on canvas with transparency based on life
 * @param {CanvasRenderingContext2D} ctx - Canvas context
 */
ParticleFirework.prototype.draw = function (ctx) {
  // TODO: Implement draw method on prototype
  // - Save context: ctx.save()
  // - Set transparency: ctx.globalAlpha = this.life
  // - Set color: ctx.fillStyle = this.color
  // - Draw circle: ctx.beginPath(), ctx.arc(x, y, size, 0, Math.PI * 2), ctx.fill()
  // - Restore context: ctx.restore()

  ctx.save();
  ctx.globalAlpha = this.life;
  ctx.fillStyle = this.color;
  ctx.beginPath();
  ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
  ctx.fill();
  ctx.restore();

  // throw new Error("ParticleFirework.draw not yet implemented");
};

/**
 * Checks if particle is dead (life depleted)
 * @returns {boolean} True if particle life is <= 0
 */
ParticleFirework.prototype.isDead = function () {
  // TODO: Return true if life <= 0

  return this.life <= 0;

  // throw new Error("ParticleFirework.isDead not yet implemented");
};

/**
 * Firework constructor - represents a rocket that explodes into particles
 * @param {number} x - Starting x position (horizontal)
 * @param {number} y - Starting y position (usually bottom of canvas)
 * @param {number} targetY - Y position where firework should explode
 * @param {string} color - Firework and particles color
 */
function Firework(x, y, targetY, color) {
  // TODO: Implement Firework constructor
  // - Validate inputs (x, y, targetY must be numbers; color must be string)
  // - Set properties: x, y, targetY, color
  // - Set exploded to false (firework hasn't exploded yet)
  // - Set particles to empty array [] (will be filled when explodes)

  if (typeof x !== 'number' || typeof y !== 'number' || typeof targetY !== 'number') {
    throw new TypeError('x, y, targetY must be numbers');
  }
  if (typeof color !== 'string') {
    throw new TypeError('color must be a string');
  }
  this.x = x;
  this.y = y;
  this.targetY = targetY;
  this.color = color;
  this.exploded = false;
  this.particles = [];

  // throw new Error("Question 5: Firework constructor not yet implemented");
}

/**
 * Updates firework state - moves upward or updates explosion particles
 */
Firework.prototype.update = function () {
  // TODO: Implement Firework update method
  // If NOT exploded:
  //   - Move upward: this.y -= 3 (rocket goes up)
  //   - Check if reached target: if (this.y <= this.targetY)
  //   - If reached target:
  //     * Set exploded = true
  //     * Create 30-50 particles at current position
  //     * Use: new ParticleFirework(this.x, this.y, this.color, 2)
  //     * Push each particle to this.particles array
  //
  // If exploded:
  //   - Call update() on each particle
  //   - Remove dead particles using filter: this.particles = this.particles.filter(p => !p.isDead())
  //
  // Hint: Random particle count: 30 + Math.floor(Math.random() * 20)

  if (!this.exploded) {
    this.y -= 3;
    if (this.y <= this.targetY) {
      this.exploded = true;
      const particleCount = 30 + Math.floor(Math.random() * 20);
      for (let i = 0; i < particleCount; i++) {
        this.particles.push(new ParticleFirework(this.x, this.y, this.color, 2));
      }
    }
  } else {
    this.particles.forEach(particle => particle.update());
    this.particles = this.particles.filter(p => !p.isDead());
  }

  // throw new Error("Firework.update not yet implemented");
};

/**
 * Draws the firework (rocket or explosion particles)
 * @param {CanvasRenderingContext2D} ctx - Canvas context
 */
Firework.prototype.draw = function (ctx) {
  // TODO: Implement Firework draw method
  // If NOT exploded:
  //   - Draw the rocket (small circle moving up)
  //   - Set ctx.fillStyle = this.color
  //   - Draw circle at this.x, this.y with radius 3
  //
  // If exploded:
  //   - Draw all particles: call draw(ctx) on each particle in this.particles

  if (!this.exploded) {
    ctx.fillStyle = this.color;
    ctx.beginPath();
    ctx.arc(this.x, this.y, 3, 0, Math.PI * 2);
    ctx.fill();
  } else {
    this.particles.forEach(particle => particle.draw(ctx));
  }

  // throw new Error("Firework.draw not yet implemented");
};

/**
 * Checks if firework is finished (exploded and all particles dead)
 * @returns {boolean} True if firework animation is complete
 */
Firework.prototype.isFinished = function () {
  // TODO: Implement isFinished method
  // - Return false if not exploded
  // - Return true if exploded AND particles array is empty (all particles dead and removed)
  //
  // Hint: Check this.exploded && this.particles.length === 0

  return this.exploded && this.particles.length === 0;

  // throw new Error("Firework.isFinished not yet implemented");
};

/**
 * Creates a fireworks show controller using closures
 * @param {HTMLCanvasElement} canvas - Canvas element for animation
 * @returns {Object} Show controller with methods: addFirework, start, stop, handleClick
 */
function createFireworksShow(canvas) {
  // TODO: Implement closure-based fireworks show
  // - Validate canvas input (must be HTMLCanvasElement)
  // - Create private variables using closures:
  //   * fireworks: [] (array to store all Firework objects)
  //   * ctx: canvas.getContext('2d')
  //   * animationId: null (will store requestAnimationFrame ID)
  //
  // - Create animate() function:
  //   1. Draw semi-transparent rectangle for fade effect:
  //      ctx.fillStyle = 'rgba(10, 10, 30, 0.1)'
  //      ctx.fillRect(0, 0, canvas.width, canvas.height)
  //   2. Update all fireworks: fireworks.forEach(fw => fw.update())
  //   3. Draw all fireworks: fireworks.forEach(fw => fw.draw(ctx))
  //   4. Remove finished fireworks: 
  //      for (let i = fireworks.length - 1; i >= 0; i--) {
  //        if (fireworks[i].isFinished()) fireworks.splice(i, 1);
  //      }
  //   5. Continue animation: animationId = requestAnimationFrame(animate)
  //
  // - Return object with methods:
  //   * addFirework(x, color): creates new Firework at (x, canvas.height) targeting 20-40% up canvas
  //   * start(): begins animation loop if not already running
  //   * stop(): cancels animation and clears canvas
  //   * handleClick(event): creates firework at click position with random color
  //
  // Hint: Target Y = canvas.height * (0.2 + Math.random() * 0.2)
  // Hint: Random color from: ['red', 'cyan', 'yellow', 'lime', 'magenta', 'orange']

  if (!(canvas instanceof HTMLCanvasElement)) {
    throw new TypeError('canvas must be an HTMLCanvasElement');
  }
  const fireworks = [];
  const ctx = canvas.getContext('2d');
  let animationId = null;

  function animate() {
    ctx.fillStyle = 'rgba(10, 10, 30, 0.1)';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    fireworks.forEach(fw => fw.update());
    fireworks.forEach(fw => fw.draw(ctx));
    for (let i = fireworks.length - 1; i >= 0; i--) {
      if (fireworks[i].isFinished()) {
        fireworks.splice(i, 1);
      }
    }
    animationId = requestAnimationFrame(animate);
  }

  const colorPalette = ['red', 'cyan', 'yellow', 'lime', 'magenta', 'orange'];

  return {
    addFirework: function (x, color) {
      const targetY = canvas.height * (0.2 + Math.random() * 0.2);
      fireworks.push(new Firework(x, canvas.height, targetY, color));
    },
    start: function () {
      if (!animationId) {
        animate();
      }
    },
    stop: function () {
      if (animationId) {
        cancelAnimationFrame(animationId);
        animationId = null;
      }
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      fireworks.length = 0;
    },
    handleClick: function (event) {
      const rect = canvas.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const randomColor = colorPalette[Math.floor(Math.random() * colorPalette.length)];
      this.addFirework(x, randomColor);
    }
  };

  // throw new Error("Question 5: createFireworksShow not yet implemented");
}


// ============================================
// EXPORTS (for testing)
// ============================================

// DO NOT MODIFY - These are used by the test framework
if (typeof module !== 'undefined' && module.exports) {
  module.exports = {
    createBankAccount,
    Circle,
    Rectangle,
    createStopwatch,
    Ball,
    createBallSimulation,
    ParticleFirework,
    Firework,
    createFireworksShow
  };
}

