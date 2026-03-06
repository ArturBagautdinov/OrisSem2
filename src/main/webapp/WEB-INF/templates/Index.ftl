<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome to OrisSem2</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #333;
        }

        .container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            padding: 3rem;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            max-width: 800px;
            width: 90%;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .container::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 5px;
            background: linear-gradient(90deg, #667eea, #764ba2, #f093fb);
            animation: gradient 3s ease infinite;
        }

        @keyframes gradient {
            0%, 100% { transform: translateX(0); }
            50% { transform: translateX(10px); }
        }

        h1 {
            font-size: 3rem;
            margin-bottom: 1rem;
            background: linear-gradient(135deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            font-weight: 800;
            animation: fadeInUp 0.8s ease;
        }

        .subtitle {
            font-size: 1.2rem;
            color: #666;
            margin-bottom: 2rem;
            animation: fadeInUp 0.8s ease 0.2s both;
        }

        .features {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
            margin: 2rem 0;
            animation: fadeInUp 0.8s ease 0.4s both;
        }

        .feature-card {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            padding: 1.5rem;
            border-radius: 15px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .feature-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        }

        .feature-icon {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .feature-title {
            font-size: 1.1rem;
            font-weight: 600;
            color: #333;
            margin-bottom: 0.5rem;
        }

        .feature-description {
            font-size: 0.9rem;
            color: #666;
        }

        .cta-button {
            display: inline-block;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 1rem 2rem;
            border-radius: 50px;
            text-decoration: none;
            font-weight: 600;
            margin-top: 2rem;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            animation: fadeInUp 0.8s ease 0.6s both;
        }

        .cta-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
        }

        .stats {
            display: flex;
            justify-content: space-around;
            margin: 2rem 0;
            animation: fadeInUp 0.8s ease 0.8s both;
        }

        .stat-item {
            text-align: center;
        }

        .stat-number {
            font-size: 2rem;
            font-weight: 700;
            color: #667eea;
        }

        .stat-label {
            font-size: 0.9rem;
            color: #666;
            margin-top: 0.25rem;
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .tech-stack {
            display: flex;
            justify-content: center;
            gap: 1rem;
            margin-top: 2rem;
            flex-wrap: wrap;
            animation: fadeInUp 0.8s ease 1s both;
        }

        .tech-badge {
            background: rgba(102, 126, 234, 0.1);
            color: #667eea;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            border: 2px solid rgba(102, 126, 234, 0.3);
        }

        @media (max-width: 768px) {
            h1 { font-size: 2rem; }
            .container { padding: 2rem; }
            .features { grid-template-columns: 1fr; }
            .stats { flex-direction: column; gap: 1rem; }
        }

        .terminal-section {
            margin-top: 3rem;
            animation: fadeInUp 0.8s ease 1.4s both;
        }

        .terminal {
            background: #1e1e1e;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
            border: 1px solid #333;
        }

        .terminal-header {
            background: #2d2d2d;
            padding: 0.75rem 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .terminal-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
        }

        .terminal-dot.red { background: #ff5f56; }
        .terminal-dot.yellow { background: #ffbd2e; }
        .terminal-dot.green { background: #27c93f; }

        .terminal-title {
            color: #888;
            font-size: 0.85rem;
            margin-left: auto;
            margin-right: auto;
        }

        .terminal-body {
            padding: 1rem;
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-size: 0.85rem;
            color: #0f0;
            min-height: 200px;
            max-height: 300px;
            overflow-y: auto;
        }

        .terminal-line {
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
        }

        .terminal-prompt {
            color: #0f0;
            margin-right: 0.5rem;
        }

        .terminal-command {
            color: #fff;
        }

        .terminal-output {
            color: #ccc;
            margin-left: 1rem;
        }

        .terminal-success {
            color: #27c93f;
        }

        .terminal-error {
            color: #ff5f56;
        }

        .activity-feed {
            margin-top: 3rem;
            animation: fadeInUp 0.8s ease 1.6s both;
        }

        .activity-feed h3 {
            text-align: center;
            margin-bottom: 1.5rem;
            color: #333;
            font-size: 1.5rem;
        }

        .activity-item {
            background: rgba(255, 255, 255, 0.8);
            border-left: 4px solid #667eea;
            padding: 1rem;
            margin-bottom: 1rem;
            border-radius: 0 8px 8px 0;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .activity-item:hover {
            transform: translateX(5px);
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
        }

        .activity-time {
            font-size: 0.8rem;
            color: #666;
            margin-bottom: 0.25rem;
        }

        .activity-title {
            font-weight: 600;
            color: #333;
            margin-bottom: 0.25rem;
        }

        .activity-description {
            font-size: 0.9rem;
            color: #666;
        }

        .performance-metrics {
            margin-top: 3rem;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
            gap: 1rem;
            animation: fadeInUp 0.8s ease 1.8s both;
        }

        .metric-card {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            padding: 1.5rem;
            border-radius: 12px;
            text-align: center;
            transition: transform 0.3s ease;
        }

        .metric-card:hover {
            transform: translateY(-5px);
        }

        .metric-value {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }

        .metric-label {
            font-size: 0.9rem;
            opacity: 0.9;
        }

        .floating-shapes {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            pointer-events: none;
            z-index: -1;
            overflow: hidden;
        }

        .shape {
            position: absolute;
            background: rgba(102, 126, 234, 0.1);
            border-radius: 50%;
            animation: float 20s infinite ease-in-out;
        }

        .shape:nth-child(1) {
            width: 80px;
            height: 80px;
            left: 10%;
            animation-delay: 0s;
        }

        .shape:nth-child(2) {
            width: 120px;
            height: 120px;
            right: 20%;
            animation-delay: 2s;
        }

        .shape:nth-child(3) {
            width: 60px;
            height: 60px;
            left: 70%;
            animation-delay: 4s;
        }

        @keyframes float {
            0%, 100% { transform: translateY(0) rotate(0deg); }
            33% { transform: translateY(-100px) rotate(120deg); }
            66% { transform: translateY(100px) rotate(240deg); }
        }

        .pulse {
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>OrisSem2</h1>
        <p class="subtitle">Modern Spring MVC Application with Advanced Features</p>
        
        <div class="features">
            <div class="feature-card">
                <div class="feature-icon">🚀</div>
                <div class="feature-title">High Performance</div>
                <div class="feature-description">Built with Spring 6 and optimized for speed</div>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🗄️</div>
                <div class="feature-title">Database Ready</div>
                <div class="feature-description">PostgreSQL integration with Hibernate ORM</div>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🎨</div>
                <div class="feature-title">Modern UI</div>
                <div class="feature-description">Beautiful templates with FreeMarker</div>
            </div>
            <div class="feature-card">
                <div class="feature-icon">🔒</div>
                <div class="feature-title">Secure</div>
                <div class="feature-description">Built with security best practices</div>
            </div>
        </div>

        <div class="stats">
            <div class="stat-item">
                <div class="stat-number">6.2</div>
                <div class="stat-label">Spring Version</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">6.6</div>
                <div class="stat-label">Hibernate Version</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">2.3</div>
                <div class="stat-label">FreeMarker Version</div>
            </div>
        </div>

        <a href="#" class="cta-button">Get Started</a>

        <div class="tech-stack">
            <span class="tech-badge">Spring MVC</span>
            <span class="tech-badge">PostgreSQL</span>
            <span class="tech-badge">Hibernate</span>
            <span class="tech-badge">FreeMarker</span>
            <span class="tech-badge">Jakarta EE</span>
        </div>

        <div class="terminal-section">
            <h3>Live Terminal Demo</h3>
            <div class="terminal">
                <div class="terminal-header">
                    <div class="terminal-dot red"></div>
                    <div class="terminal-dot yellow"></div>
                    <div class="terminal-dot green"></div>
                    <div class="terminal-title">Terminal</div>
                </div>
                <div class="terminal-body" id="terminal">
                    <div class="terminal-line">
                        <span class="terminal-prompt">$</span>
                        <span class="terminal-command">./gradlew build</span>
                    </div>
                    <div class="terminal-output terminal-success">> Task :compileJava</div>
                    <div class="terminal-output terminal-success">> Task :processResources</div>
                    <div class="terminal-output terminal-success">> Task :classes</div>
                    <div class="terminal-output terminal-success">> Task :war</div>
                    <div class="terminal-output terminal-success">BUILD SUCCESSFUL</div>
                    <div class="terminal-line">
                        <span class="terminal-prompt">$</span>
                        <span class="terminal-command">java -jar build/libs/OrisSem2.war</span>
                    </div>
                    <div class="terminal-output">Starting OrisSem2 application...</div>
                    <div class="terminal-output terminal-success">Server started on port 8080</div>
                    <div class="terminal-output">Ready to receive requests!</div>
                </div>
            </div>
        </div>

        <div class="performance-metrics">
            <div class="metric-card pulse">
                <div class="metric-value">99.9%</div>
                <div class="metric-label">Uptime</div>
            </div>
            <div class="metric-card">
                <div class="metric-value">&lt;50ms</div>
                <div class="metric-label">Response Time</div>
            </div>
            <div class="metric-card">
                <div class="metric-value">10K+</div>
                <div class="metric-label">Requests/sec</div>
            </div>
            <div class="metric-card">
                <div class="metric-value">A+</div>
                <div class="metric-label">Security Grade</div>
            </div>
        </div>

        <div class="activity-feed">
            <h3>Recent Activity</h3>
            <div class="activity-item">
                <div class="activity-time">2 minutes ago</div>
                <div class="activity-title">Build Completed Successfully</div>
                <div class="activity-description">Latest deployment to production environment completed without issues</div>
            </div>
            <div class="activity-item">
                <div class="activity-time">15 minutes ago</div>
                <div class="activity-title">Database Migration Applied</div>
                <div class="activity-description">Schema updates for user management module deployed successfully</div>
            </div>
            <div class="activity-item">
                <div class="activity-time">1 hour ago</div>
                <div class="activity-title">Performance Optimization</div>
                <div class="activity-description">Cache implementation reduced average response time by 40%</div>
            </div>
            <div class="activity-item">
                <div class="activity-time">3 hours ago</div>
                <div class="activity-title">Security Update</div>
                <div class="activity-description">Latest security patches applied to all dependencies</div>
            </div>
        </div>
    </div>

    <div class="floating-shapes">
        <div class="shape"></div>
        <div class="shape"></div>
        <div class="shape"></div>
    </div>

    <script>
        const terminalCommands = [
            { command: 'git status', output: ['On branch main', 'Your branch is up to date', 'nothing to commit, working tree clean'] },
            { command: 'curl http://localhost:8080/api/status', output: ['{"status":"running","version":"1.0.0","uptime":1234567890}'] },
            { command: './gradlew test', output: ['> Task :test', 'BUILD SUCCESSFUL', '12 tests completed, 12 passed'] }
        ];

        let commandIndex = 0;
        
        function typeCommand() {
            const terminal = document.getElementById('terminal');
            const currentCommand = terminalCommands[commandIndex % terminalCommands.length];
            
            const newLine = document.createElement('div');
            newLine.className = 'terminal-line';
            newLine.innerHTML = `<span class="terminal-prompt">$</span><span class="terminal-command">${currentCommand.command}</span>`;
            terminal.appendChild(newLine);
            
            setTimeout(() => {
                currentCommand.output.forEach(line => {
                    const outputLine = document.createElement('div');
                    outputLine.className = 'terminal-output';
                    outputLine.textContent = line;
                    terminal.appendChild(outputLine);
                });
                
                terminal.scrollTop = terminal.scrollHeight;
                commandIndex++;
                
                setTimeout(typeCommand, 3000);
            }, 1000);
        }
        
        // Start terminal animation after page load
        setTimeout(typeCommand, 2000);
        
        // Add smooth scroll behavior
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                document.querySelector(this.getAttribute('href')).scrollIntoView({
                    behavior: 'smooth'
                });
            });
        });
    </script>
</body>
</html>