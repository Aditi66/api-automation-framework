# GitHub Pages Setup Guide

This guide will help you set up GitHub Pages to host your API Automation Framework documentation.

## Prerequisites

- A GitHub repository with the API Automation Framework code
- Admin access to the repository settings
- Node.js 18+ installed locally (for testing)

## Step 1: Enable GitHub Pages

1. **Go to your repository on GitHub**
2. **Navigate to Settings** → **Pages**
3. **Under "Source"**, select **"GitHub Actions"**
4. **Save the settings**

## Step 2: Configure GitHub Actions

The repository already includes the necessary GitHub Actions workflow file:
- `.github/workflows/deploy-docs.yml`

This workflow will:
- Build the documentation using VitePress
- Deploy it to GitHub Pages
- Run automatically on pushes to main/master branch

## Step 3: Install Dependencies

The project includes a `package.json` file with the necessary dependencies. The GitHub Actions workflow will handle this automatically, but for local development:

```bash
# Install Node.js dependencies
npm install

# Or if you prefer yarn
yarn install
```

## Step 4: Test Locally (Optional)

Before pushing to GitHub, you can test the documentation locally:

```bash
# Start development server
npm run dev

# Or build and preview
npm run build
npm run preview
```

The documentation will be available at `http://localhost:5173`

## Step 5: Push and Deploy

1. **Commit and push your changes**:
   ```bash
   git add .
   git commit -m "Add documentation and GitHub Pages setup"
   git push origin main
   ```

2. **Check the GitHub Actions tab** in your repository to monitor the deployment

3. **Wait for the workflow to complete** (usually takes 2-3 minutes)

## Step 6: Access Your Documentation

Once the deployment is complete, your documentation will be available at:
```
https://[your-username].github.io/api-automation-framework/
```

## Customization

### Update Repository Name

If your repository has a different name, update the base URL in:
- `docs/.vitepress/config.ts` - Change the `base` property
- `package.json` - Update the repository URL

### Custom Domain (Optional)

To use a custom domain:

1. **Add a CNAME file** to the `docs/public/` directory with your domain
2. **Configure DNS** to point to your GitHub Pages URL
3. **Update GitHub Pages settings** to use your custom domain

## Troubleshooting

### Build Failures

If the GitHub Actions build fails:

1. **Check the Actions tab** for detailed error messages
2. **Verify Node.js version** - The workflow uses Node.js 18
3. **Check file paths** - Ensure all documentation files are in the correct locations

### Documentation Not Updating

If changes aren't reflected:

1. **Wait a few minutes** - GitHub Pages can take time to update
2. **Check the Actions tab** - Ensure the workflow completed successfully
3. **Clear browser cache** - Try a hard refresh (Ctrl+F5)

### Local Development Issues

If you can't run the documentation locally:

1. **Check Node.js version** - Ensure you have Node.js 18+
2. **Clear npm cache** - Run `npm cache clean --force`
3. **Delete node_modules** - Remove and reinstall dependencies

## File Structure

The documentation setup includes:

```
docs/
├── .vitepress/
│   └── config.ts              # VitePress configuration
├── index.md                   # Home page
├── getting-started.md         # Getting started guide
├── api-reference.md           # API reference
├── best-practices.md          # Best practices
├── troubleshooting.md         # Troubleshooting guide
└── public/                    # Static assets (optional)
    └── favicon.ico

.github/workflows/
└── deploy-docs.yml           # GitHub Actions workflow

package.json                   # Node.js dependencies
```

## Maintenance

### Updating Documentation

1. **Edit the Markdown files** in the `docs/` directory
2. **Commit and push** your changes
3. **GitHub Actions will automatically rebuild and deploy**

### Adding New Pages

1. **Create a new Markdown file** in the `docs/` directory
2. **Update the navigation** in `docs/.vitepress/config.ts`
3. **Commit and push** your changes

### Updating Dependencies

To update VitePress or other dependencies:

1. **Update package.json** with new versions
2. **Test locally** with `npm run dev`
3. **Commit and push** the changes

## Support

If you encounter issues:

1. **Check the GitHub Actions logs** for build errors
2. **Review the VitePress documentation** at https://vitepress.dev/
3. **Create an issue** in your repository with detailed error information

---

Your documentation will be automatically updated whenever you push changes to the main branch. The GitHub Actions workflow handles the build and deployment process seamlessly.
