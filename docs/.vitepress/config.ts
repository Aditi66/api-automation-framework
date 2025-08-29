import { defineConfig } from 'vitepress'

export default defineConfig({
  title: 'API Automation Framework',
  description: 'A comprehensive framework for API testing with REST Assured, Cucumber, and TestNG',
  
  // Base URL for GitHub Pages
  base: '/api-automation-framework/',
  
  // Theme configuration
  themeConfig: {
    siteTitle: 'API Automation Framework',
    
    // Navigation
    nav: [
      { text: 'Home', link: '/' },
      { text: 'Getting Started', link: '/getting-started' },
      { text: 'API Reference', link: '/api-reference' },
      { text: 'Best Practices', link: '/best-practices' },
      { text: 'Troubleshooting', link: '/troubleshooting' },
      { 
        text: 'GitHub', 
        link: 'https://github.com/Aditi66/api-automation-framework',
        target: '_blank'
      }
    ],
    
    // Sidebar
    sidebar: {
      '/': [
        {
          text: 'Introduction',
          items: [
            { text: 'Overview', link: '/' },
            { text: 'Quick Start', link: '/quick-start' },
            { text: 'Getting Started', link: '/getting-started' }
          ]
        },
        {
          text: 'Reference',
          items: [
            { text: 'API Reference', link: '/api-reference' },
            { text: 'Configuration', link: '/configuration' },
            { text: 'Validation', link: '/validation' }
          ]
        },
        {
          text: 'Guides',
          items: [
            { text: 'Best Practices', link: '/best-practices' },
            { text: 'Troubleshooting', link: '/troubleshooting' },
            { text: 'Examples', link: '/examples' }
          ]
        }
      ]
    },
    
    // Social links
    socialLinks: [
      { 
        icon: 'github', 
        link: 'https://github.com/Aditi66/api-automation-framework' 
      }
    ],
    
    // Footer
    footer: {
      message: 'Released under the MIT License.',
      copyright: 'Copyright © 2024-present API Automation Framework'
    },
    
    // Search
    search: {
      provider: 'local'
    },
    
    // Edit link
    editLink: {
      pattern: 'https://github.com/Aditi66/api-automation-framework/edit/main/docs/:path'
    },
    
    // Last updated
    lastUpdated: true,
    
    // Dark mode
    darkModeSwitchLabel: 'Appearance',
    
    // Mobile navigation
    mobileNav: {
      text: 'Menu',
      items: [
        { text: 'Home', link: '/' },
        { text: 'Getting Started', link: '/getting-started' },
        { text: 'API Reference', link: '/api-reference' },
        { text: 'Best Practices', link: '/best-practices' },
        { text: 'Troubleshooting', link: '/troubleshooting' }
      ]
    }
  },
  
  // Markdown configuration
  markdown: {
    theme: 'material-theme-palenight',
    lineNumbers: true,
    toc: {
      level: [1, 2, 3]
    }
  },
  
  // Head configuration
  head: [
    ['link', { rel: 'icon', href: '/favicon.ico' }],
    ['meta', { name: 'theme-color', content: '#646cff' }],
    ['meta', { name: 'viewport', content: 'width=device-width, initial-scale=1.0' }]
  ],
  
  // Build configuration
  vite: {
    build: {
      rollupOptions: {
        output: {
          manualChunks: {
            'markdown-it': ['markdown-it'],
            'prismjs': ['prismjs']
          }
        }
      }
    }
  }
})
