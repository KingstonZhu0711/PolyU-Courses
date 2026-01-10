/**
 * Test Framework for Assignment 2 - Independent Questions
 * DO NOT MODIFY THIS FILE
 */

// ============================================
// UTILITY FUNCTIONS
// ============================================

function logOutput(elementId, message, type = 'info') {
  const output = document.getElementById(elementId);
  const timestamp = new Date().toLocaleTimeString();
  const typeClass = type === 'error' ? 'error' : type === 'success' ? 'success' : 'info';
  output.innerHTML += `<div class="log-entry ${typeClass}">[${timestamp}] ${message}</div>`;
  output.scrollTop = output.scrollHeight;
}

function formatCurrency(amount) {
  return '$' + amount.toFixed(2);
}

function formatTime(ms) {
  const totalSeconds = Math.floor(ms / 1000);
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = totalSeconds % 60;
  const milliseconds = ms % 1000;
  return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}.${String(milliseconds).padStart(3, '0')}`;
}

// ============================================
// QUESTION 1 TESTS - BANK ACCOUNT
// ============================================

let accountA = null;
let accountB = null;

function initQ1() {
  try {
    accountA = createBankAccount(0, 'Alice');
    accountB = createBankAccount(0, 'Bob');
    logOutput('q1-output', 'Bank accounts initialized successfully', 'success');
  } catch (error) {
    logOutput('q1-output', `Error initializing accounts: ${error.message}`, 'error');
  }
}

function testQ1(account, action, amount) {
  const targetAccount = account === 'a' ? accountA : accountB;
  const displayId = `account-${account}-display`;
  const accountName = account === 'a' ? 'Alice' : 'Bob';
  
  if (!targetAccount) {
    initQ1();
    return;
  }
  
  try {
    if (action === 'deposit') {
      targetAccount.deposit(amount);
      logOutput('q1-output', `${accountName}: Deposited ${formatCurrency(amount)}`, 'success');
    } else if (action === 'withdraw') {
      targetAccount.withdraw(amount);
      logOutput('q1-output', `${accountName}: Withdrew ${formatCurrency(amount)}`, 'success');
    } else if (action === 'history') {
      const history = targetAccount.getTransactionHistory();
      logOutput('q1-output', `${accountName}'s Transaction History (${history.length} transactions):`, 'info');
      history.forEach((tx, i) => {
        logOutput('q1-output', `  ${i+1}. ${tx.type.toUpperCase()}: ${formatCurrency(tx.amount)} → Balance: ${formatCurrency(tx.balance)}`, 'info');
      });
      return;
    }
    
    const balance = targetAccount.getBalance();
    document.getElementById(displayId).textContent = `Balance: ${formatCurrency(balance)}`;
  } catch (error) {
    logOutput('q1-output', `${accountName} Error: ${error.message}`, 'error');
  }
}

// Initialize Q1 on load
window.addEventListener('load', () => {
  setTimeout(initQ1, 100);
});

// ============================================
// QUESTION 2 TESTS - GEOMETRIC SHAPES
// ============================================

let q2Shapes = [];
let q2SelectedShape = null;

function testQ2(action) {
  const canvas = document.getElementById('q2-canvas');
  const ctx = canvas.getContext('2d');
  
  try {
    if (action === 'circle') {
      const x = Math.random() * (canvas.width - 100) + 50;
      const y = Math.random() * (canvas.height - 100) + 50;
      const radius = 20 + Math.random() * 30;
      const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA07A', '#98D8C8'];
      const color = colors[Math.floor(Math.random() * colors.length)];
      
      const circle = new Circle(x, y, radius, color);
      q2Shapes.push(circle);
      logOutput('q2-output', `Added circle at (${Math.round(x)}, ${Math.round(y)}) - Area: ${circle.getArea().toFixed(2)}, Perimeter: ${circle.getPerimeter().toFixed(2)}`, 'success');
    } else if (action === 'rectangle') {
      const x = Math.random() * (canvas.width - 120) + 20;
      const y = Math.random() * (canvas.height - 120) + 20;
      const width = 40 + Math.random() * 60;
      const height = 40 + Math.random() * 60;
      const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA07A', '#98D8C8'];
      const color = colors[Math.floor(Math.random() * colors.length)];
      
      const rect = new Rectangle(x, y, width, height, color);
      q2Shapes.push(rect);
      logOutput('q2-output', `Added rectangle at (${Math.round(x)}, ${Math.round(y)}) - Area: ${rect.getArea().toFixed(2)}, Perimeter: ${rect.getPerimeter().toFixed(2)}`, 'success');
    } else if (action === 'random') {
      testQ2(Math.random() > 0.5 ? 'circle' : 'rectangle');
      return;
    } else if (action === 'clear') {
      q2Shapes = [];
      q2SelectedShape = null;
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      logOutput('q2-output', 'Cleared all shapes', 'info');
      return;
    }
    
    drawQ2Shapes();
  } catch (error) {
    logOutput('q2-output', `Error: ${error.message}`, 'error');
  }
}

function drawQ2Shapes() {
  const canvas = document.getElementById('q2-canvas');
  const ctx = canvas.getContext('2d');
  
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  
  q2Shapes.forEach(shape => {
    try {
      shape.draw(ctx);
      
      // Draw selection highlight
      if (shape === q2SelectedShape) {
        ctx.strokeStyle = 'yellow';
        ctx.lineWidth = 3;
        ctx.beginPath();
        if (shape.radius !== undefined) {
          ctx.arc(shape.x, shape.y, shape.radius + 5, 0, Math.PI * 2);
        } else if (shape.width !== undefined) {
          ctx.rect(shape.x - 5, shape.y - 5, shape.width + 10, shape.height + 10);
        }
        ctx.stroke();
      }
    } catch (error) {
      console.error('Draw error:', error);
    }
  });
}

// Handle canvas clicks for Q2
document.addEventListener('DOMContentLoaded', () => {
  const canvas = document.getElementById('q2-canvas');
  if (canvas) {
    canvas.addEventListener('click', (event) => {
      const rect = canvas.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      
      // Check if clicked on any shape
      for (let i = q2Shapes.length - 1; i >= 0; i--) {
        const shape = q2Shapes[i];
        try {
          if (shape.contains(x, y)) {
            q2SelectedShape = shape;
            const type = shape.radius !== undefined ? 'Circle' : 'Rectangle';
            logOutput('q2-output', `Selected ${type} - Area: ${shape.getArea().toFixed(2)}, Perimeter: ${shape.getPerimeter().toFixed(2)}`, 'success');
            drawQ2Shapes();
            return;
          }
        } catch (error) {
          console.error('Contains error:', error);
        }
      }
      
      q2SelectedShape = null;
      drawQ2Shapes();
      logOutput('q2-output', `Clicked at (${Math.round(x)}, ${Math.round(y)}) - No shape`, 'info');
    });
  }
});

// ============================================
// QUESTION 3 TESTS - STOPWATCH
// ============================================

let stopwatchA = null;
let stopwatchB = null;
let stopwatchAInterval = null;
let stopwatchBInterval = null;

function initQ3() {
  try {
    stopwatchA = createStopwatch();
    stopwatchB = createStopwatch();
    logOutput('q3-output', 'Stopwatches initialized successfully', 'success');
  } catch (error) {
    logOutput('q3-output', `Error initializing stopwatches: ${error.message}`, 'error');
  }
}

function updateStopwatchDisplay(watch, displayId) {
  try {
    const time = watch.getTime();
    document.getElementById(displayId).textContent = formatTime(time);
  } catch (error) {
    console.error('Display update error:', error);
  }
}

function testQ3(watch, action) {
  const targetWatch = watch === 'a' ? stopwatchA : stopwatchB;
  const displayId = `stopwatch-${watch}-display`;
  const watchName = watch === 'a' ? 'Stopwatch 1' : 'Stopwatch 2';
  
  if (!targetWatch) {
    initQ3();
    return;
  }
  
  try {
    if (action === 'start') {
      targetWatch.start();
      logOutput('q3-output', `${watchName}: Started`, 'success');
      
      // Start display update
      if (watch === 'a') {
        if (stopwatchAInterval) clearInterval(stopwatchAInterval);
        stopwatchAInterval = setInterval(() => updateStopwatchDisplay(targetWatch, displayId), 10);
      } else {
        if (stopwatchBInterval) clearInterval(stopwatchBInterval);
        stopwatchBInterval = setInterval(() => updateStopwatchDisplay(targetWatch, displayId), 10);
      }
    } else if (action === 'stop') {
      const time = targetWatch.stop();
      logOutput('q3-output', `${watchName}: Stopped at ${formatTime(time)}`, 'info');
      
      // Stop display update
      if (watch === 'a' && stopwatchAInterval) {
        clearInterval(stopwatchAInterval);
        stopwatchAInterval = null;
      } else if (watch === 'b' && stopwatchBInterval) {
        clearInterval(stopwatchBInterval);
        stopwatchBInterval = null;
      }
      updateStopwatchDisplay(targetWatch, displayId);
    } else if (action === 'reset') {
      targetWatch.reset();
      logOutput('q3-output', `${watchName}: Reset to 0`, 'info');
      document.getElementById(displayId).textContent = '00:00.000';
    } else if (action === 'lap') {
      targetWatch.lap();
      const time = targetWatch.getTime();
      logOutput('q3-output', `${watchName}: Lap recorded at ${formatTime(time)}`, 'success');
    } else if (action === 'laps') {
      const laps = targetWatch.getLaps();
      logOutput('q3-output', `${watchName} Laps (${laps.length} total):`, 'info');
      laps.forEach((lap, i) => {
        logOutput('q3-output', `  Lap ${i+1}: ${formatTime(lap)}`, 'info');
      });
    }
  } catch (error) {
    logOutput('q3-output', `${watchName} Error: ${error.message}`, 'error');
  }
}

// Initialize Q3 on load
window.addEventListener('load', () => {
  setTimeout(initQ3, 100);
});

// ============================================
// QUESTION 4 TESTS - BOUNCING BALLS
// ============================================

let ballSimulation = null;
let gravityEnabled = false;

function initQ4() {
  const canvas = document.getElementById('q4-canvas');
  
  try {
    ballSimulation = createBallSimulation(canvas);
    logOutput('q4-output', 'Ball simulation initialized successfully', 'success');
    
    // Add click handler
    canvas.addEventListener('click', (event) => {
      const rect = canvas.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;
      
      try {
        const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA07A', '#98D8C8', '#F7DC6F'];
        const color = colors[Math.floor(Math.random() * colors.length)];
        const radius = 10 + Math.random() * 20;
        
        ballSimulation.addBall(x, y, radius, color);
        logOutput('q4-output', `Added ball at (${Math.round(x)}, ${Math.round(y)})`, 'success');
      } catch (error) {
        logOutput('q4-output', `Click error: ${error.message}`, 'error');
      }
    });
  } catch (error) {
    logOutput('q4-output', `Error initializing simulation: ${error.message}`, 'error');
  }
}

function testQ4(action) {
  if (!ballSimulation && action !== 'stop') {
    initQ4();
    if (!ballSimulation) return;
  }
  
  try {
    if (action === 'start') {
      ballSimulation.start();
      logOutput('q4-output', 'Animation started', 'success');
    } else if (action === 'stop') {
      if (ballSimulation) {
        ballSimulation.stop();
      }
      logOutput('q4-output', 'Animation stopped', 'info');
    } else if (action === 'gravity') {
      gravityEnabled = !gravityEnabled;
      ballSimulation.setGravity(gravityEnabled);
      logOutput('q4-output', `Gravity ${gravityEnabled ? 'enabled' : 'disabled'}`, 'info');
      document.getElementById('q4-info').textContent = 
        gravityEnabled ? 'Gravity ON - Balls fall down!' : 'Gravity OFF - Balls bounce freely!';
    } else if (action === 'addRandom') {
      const canvas = document.getElementById('q4-canvas');
      const colors = ['#FF6B6B', '#4ECDC4', '#45B7D1', '#FFA07A', '#98D8C8', '#F7DC6F'];
      
      for (let i = 0; i < 5; i++) {
        const x = Math.random() * canvas.width;
        const y = Math.random() * canvas.height;
        const radius = 10 + Math.random() * 20;
        const color = colors[Math.floor(Math.random() * colors.length)];
        ballSimulation.addBall(x, y, radius, color);
      }
      logOutput('q4-output', 'Added 5 random balls', 'success');
    } else if (action === 'clear') {
      ballSimulation.clear();
      logOutput('q4-output', 'Cleared all balls', 'info');
    }
  } catch (error) {
    logOutput('q4-output', `Error: ${error.message}`, 'error');
  }
}

// Initialize Q4 on load
window.addEventListener('load', () => {
  setTimeout(initQ4, 100);
});

// ============================================
// QUESTION 5 TESTS - FIREWORKS
// ============================================

let fireworksShow = null;
let autoLaunchInterval = null;

function initQ5() {
  const canvas = document.getElementById('q5-canvas');
  
  try {
    fireworksShow = createFireworksShow(canvas);
    
    // Add click handler
    canvas.addEventListener('click', (event) => {
      try {
        fireworksShow.handleClick(event);
        logOutput('q5-output', `Firework launched at (${Math.round(event.offsetX)}, ${Math.round(event.offsetY)})`, 'success');
      } catch (error) {
        logOutput('q5-output', `Click error: ${error.message}`, 'error');
      }
    });
    
    logOutput('q5-output', 'Fireworks show initialized! Click canvas to launch fireworks.', 'success');
  } catch (error) {
    logOutput('q5-output', `Error initializing show: ${error.message}`, 'error');
  }
}

function testQ5(action) {
  if (!fireworksShow && action !== 'stop') {
    initQ5();
    if (!fireworksShow) return;
  }
  
  try {
    if (action === 'start') {
      fireworksShow.start();
      logOutput('q5-output', 'Animation started', 'success');
    } else if (action === 'stop') {
      if (fireworksShow) {
        fireworksShow.stop();
      }
      if (autoLaunchInterval) {
        clearInterval(autoLaunchInterval);
        autoLaunchInterval = null;
      }
      logOutput('q5-output', 'Animation stopped', 'info');
    } else if (action === 'auto') {
      const canvas = document.getElementById('q5-canvas');
      const colors = ['red', 'cyan', 'yellow', 'lime', 'magenta', 'orange'];
      
      if (autoLaunchInterval) {
        clearInterval(autoLaunchInterval);
        autoLaunchInterval = null;
        logOutput('q5-output', 'Auto launch stopped', 'info');
      } else {
        autoLaunchInterval = setInterval(() => {
          const x = canvas.width * (0.1 + Math.random() * 0.8);
          const color = colors[Math.floor(Math.random() * colors.length)];
          fireworksShow.addFirework(x, color);
        }, 600);
        logOutput('q5-output', 'Auto launch started', 'success');
      }
    } else if (action === 'clear') {
      const canvas = document.getElementById('q5-canvas');
      const ctx = canvas.getContext('2d');
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      logOutput('q5-output', 'Canvas cleared', 'info');
    }
  } catch (error) {
    logOutput('q5-output', `Error: ${error.message}`, 'error');
  }
}

// Initialize Q5 on load
window.addEventListener('load', () => {
  setTimeout(initQ5, 100);
});

// ============================================
// UTILITY: Clear old logs
// ============================================

setInterval(() => {
  ['q1-output', 'q2-output', 'q3-output', 'q4-output', 'q5-output'].forEach(id => {
    const output = document.getElementById(id);
    if (output && output.children.length > 50) {
      while (output.children.length > 30) {
        output.removeChild(output.firstChild);
      }
    }
  });
}, 10000);

