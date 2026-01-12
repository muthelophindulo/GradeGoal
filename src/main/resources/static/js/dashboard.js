/**
 * Dashboard page specific JavaScript
 */

// DOM Elements
let menuToggle, closeSidebar, sidebar, header;
let statsCards;

/**
 * Initialize dashboard elements
 */
function initDashboardElements() {
    menuToggle = document.getElementById('menuToggle');
    closeSidebar = document.getElementById('closeSidebar');
    sidebar = document.getElementById('sidebar');
    header = document.getElementById('header');
    statsCards = document.querySelectorAll('.stats-card');
}

/**
 * Initialize mobile menu toggle
 */
function initMobileMenu() {
    if (!menuToggle || !closeSidebar || !sidebar) return;
    
    // Open sidebar
    menuToggle.addEventListener('click', function() {
        sidebar.classList.add('active');
        document.body.style.overflow = 'hidden';
    });
    
    // Close sidebar
    closeSidebar.addEventListener('click', function() {
        sidebar.classList.remove('active');
        document.body.style.overflow = 'auto';
    });
    
    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', function(event) {
        if (window.innerWidth <= 992 && 
            !sidebar.contains(event.target) && 
            !menuToggle.contains(event.target) && 
            sidebar.classList.contains('active')) {
            sidebar.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
    });
    
    // Close sidebar on escape key
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && sidebar.classList.contains('active')) {
            sidebar.classList.remove('active');
            document.body.style.overflow = 'auto';
        }
    });
}

/**
 * Animate stats numbers with counting effect
 */
function animateStats() {
    const statsNumbers = document.querySelectorAll('.stats-number');
    
    statsNumbers.forEach(number => {
        const text = number.textContent;
        if (text.includes('%') || text.includes('.')) {
            const finalValue = parseFloat(text);
            let startValue = 0;
            const duration = 1500;
            const increment = finalValue / (duration / 16);
            
            const timer = setInterval(() => {
                startValue += increment;
                if (startValue >= finalValue) {
                    number.textContent = text;
                    clearInterval(timer);
                } else {
                    number.textContent = text.includes('.') 
                        ? startValue.toFixed(2) 
                        : Math.floor(startValue);
                }
            }, 16);
        }
    });
}

/**
 * Add scroll effect to header on desktop
 */
function initHeaderScrollEffect() {
    if (!header) return;
    
    window.addEventListener('scroll', function() {
        if (window.innerWidth > 992) {
            if (window.scrollY > 10) {
                header.style.boxShadow = '0 5px 20px rgba(0, 0, 0, 0.1)';
                header.style.padding = '15px 30px';
            } else {
                header.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.05)';
                header.style.padding = '20px 30px';
            }
        }
    });
}

/**
 * Add click effects to stats cards
 */
function initStatsCards() {
    if (!statsCards || statsCards.length === 0) return;
    
    statsCards.forEach(card => {
        card.addEventListener('click', function(e) {
            // Only add effect if card doesn't have a link
            if (!this.getAttribute('href')) {
                this.style.transform = 'scale(0.98)';
                setTimeout(() => {
                    this.style.transform = '';
                }, 150);
            }
        });
    });
}

/**
 * Handle window resize events
 */
function initWindowResizeHandler() {
    let resizeTimer;
    
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimer);
        resizeTimer = setTimeout(function() {
            // Close sidebar if open on mobile when resizing to desktop
            if (window.innerWidth > 992 && sidebar && sidebar.classList.contains('active')) {
                sidebar.classList.remove('active');
                document.body.style.overflow = 'auto';
            }
        }, 250);
    });
}

/**
 * Adjust goal items for dynamic content
 */
function adjustGoalItems() {
    const goalItems = document.querySelectorAll('.goal-item');
    
    goalItems.forEach(item => {
        // Check if content is overflowing
        if (item.scrollHeight > item.clientHeight) {
            // Reduce padding for tight fit
            item.style.padding = '8px 12px';
            const subtext = item.querySelector('.goal-subtext');
            if (subtext) {
                subtext.style.fontSize = '0.75rem';
            }
        }
    });
}

/**
 * Initialize course rank item hover effects
 */
function initCourseRankEffects() {
    const courseRankItems = document.querySelectorAll('.course-rank-item');
    
    courseRankItems.forEach(item => {
        item.addEventListener('mouseenter', function() {
            this.style.transform = 'translateX(5px)';
        });
        
        item.addEventListener('mouseleave', function() {
            this.style.transform = 'translateX(0)';
        });
    });
}

/**
 * Initialize grade item hover effects
 */
function initGradeItemEffects() {
    const gradeItems = document.querySelectorAll('.grade-item');
    
    gradeItems.forEach(item => {
        item.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#e9ecef';
        });
        
        item.addEventListener('mouseleave', function() {
            this.style.backgroundColor = '#f8f9fa';
        });
    });
}

/**
 * Initialize deadline item hover effects
 */
function initDeadlineItemEffects() {
    const deadlineItems = document.querySelectorAll('.deadline-item');
    
    deadlineItems.forEach(item => {
        item.addEventListener('mouseenter', function() {
            this.style.backgroundColor = '#e9ecef';
        });
        
        item.addEventListener('mouseleave', function() {
            this.style.backgroundColor = '#f8f9fa';
        });
    });
}

/**
 * Update welcome message with current time
 */
function updateWelcomeMessage() {
    const welcomeText = document.querySelector('.welcome-text h2');
    if (!welcomeText) return;
    
    const now = new Date();
    const hour = now.getHours();
    let greeting = 'Welcome';
    
    if (hour < 12) {
        greeting = 'Good morning';
    } else if (hour < 18) {
        greeting = 'Good afternoon';
    } else {
        greeting = 'Good evening';
    }
    
    // Only update if it's the default welcome message
    if (welcomeText.textContent.includes('Welcome back')) {
        const userName = welcomeText.textContent.replace('Welcome back, ', '').replace('!', '');
        welcomeText.textContent = `${greeting}, ${userName}!`;
    }
}

/**
 * Initialize real-time clock in header
 */
function initRealTimeClock() {
    const clockElement = document.getElementById('realTimeClock');
    if (!clockElement) return;
    
    function updateClock() {
        const now = new Date();
        const timeString = now.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
        clockElement.textContent = timeString;
    }
    
    // Update immediately and then every minute
    updateClock();
    setInterval(updateClock, 60000);
}

/**
 * Initialize all dashboard functionality
 */
function initDashboard() {
    // Initialize elements
    initDashboardElements();
    
    // Set up event listeners and effects
    initMobileMenu();
    initHeaderScrollEffect();
    initStatsCards();
    initWindowResizeHandler();
    initCourseRankEffects();
    initGradeItemEffects();
    initDeadlineItemEffects();
    
    // Run animations and updates
    animateStats();
    adjustGoalItems();
    updateWelcomeMessage();
    initRealTimeClock();
    
    // Adjust goal items on resize
    window.addEventListener('resize', adjustGoalItems);
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', initDashboard);