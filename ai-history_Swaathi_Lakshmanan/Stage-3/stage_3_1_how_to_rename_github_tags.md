# How to Rename GitHub Tags

**Source:** Gemini (`https://gemini.google.com/app/8cb5873022ab57b0`)  
**Date:** 11/14/26, 7:34 PM

---

### User
how do I rename tags on github

---

### Gemini
GitHub does not provide a direct user interface button to rename a tag, and Git itself lacks a single-step command for it. To rename a tag, you need to create a new tag with the desired name, delete the old tag locally, and then update your remote repository on GitHub.

#### 1. Create the new tag locally
Run the following command in your terminal to create a new tag pointing to the same commit as your old tag:

```bash
git tag <new_tag_name> <old_tag_name>
```

> **Note:** If your old tag was an annotated tag and you wish to preserve its message, you can use `git tag -a <new_tag_name> <old_tag_name>^{} -m "Your tag message"` instead.

#### 2. Delete the old tag locally
Remove the old tag from your local machine:

```bash
git tag -d <old_tag_name>
```

#### 3. Delete the old tag from GitHub
Push a deletion command to your remote repository (`origin`):

```bash
git push origin :refs/tags/<old_tag_name>
```

Alternatively, you can use:

```bash
git push --delete origin <old_tag_name>
```

#### 4. Push the new tag to GitHub
Upload your newly created tag to the remote repository:

```bash
git push origin <new_tag_name>
```

#### Important considerations for collaboration
If other developers have already cloned or fetched your repository, their local environments will still hold the old tag. You should communicate the change clearly to your team so they can update their local tag references. They can synchronise their local tags by running:

```bash
git fetch --prune --tags
```

This command cleans up deleted remote tags and fetches any new ones, ensuring consistent project behaviour across all local copies.

**Sources:**
1. How Do You Rename a Git Tag? | Better Stack Community