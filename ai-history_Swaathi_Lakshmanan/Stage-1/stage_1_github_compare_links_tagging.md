# How to Get GitHub Compare Links

**Source:** Gemini (`https://gemini.google.com/app/a3b85fc26ced79cd`)  
**Date:** 10/15/26, 2:39 PM

---

### User
hey how do I get compare links on github

---

### Gemini
You can easily get or construct compare links on GitHub by using the direct URL pattern or by using the web interface.

#### 1. The Direct URL Structure
The cleanest way to create a compare link is to follow this URL format:

`https://github.com/OWNER/REPOSITORY/compare/base...compare`

* **OWNER:** Your GitHub username or organisation name.
* **REPOSITORY:** The name of the repository.
* **base:** The starting point or reference (e.g., `main` or an older release tag).
* **compare:** The branch, tag, or commit containing the new changes.

**Common Examples:**
* **Branches:** `https://github.com/username/repo/compare/main...feature-branch`
* **Tags (Releases):** `https://github.com/username/repo/compare/v1.0.0...v1.1.0`
* **Commits (using SHAs):** `https://github.com/username/repo/compare/f75c570...3391dcc`

#### 2. Using the GitHub Web Interface
If you prefer not to write the URL by hand, you can generate it through GitHub's interface:

1. **The Compare Path:** Simply append `/compare` to your repository's main URL (e.g., `https://github.com/username/repo/compare`) to open GitHub's native comparison tool.
2. **The Pull Request Trick:** Go to the **Pull requests** tab in your repository, click **New pull request**, and select your base and compare branches. This generates the exact comparison view and URL you need without forcing you to actually create the pull request. Once the view loads, you can copy the URL straight from your browser's address bar.

#### 3. Handy Tricks
* **Plain Text Diffs:** You can append `.diff` or `.patch` to the end of any compare URL to view or download the raw text file of the changes (e.g., `https://github.com/username/repo/compare/main...feature-branch.diff`).
* **Ignoring Whitespace:** If you want to share a link that automatically hides whitespace changes, you can add `?w=1` to the end of the URL.

**Sources:**
1. GitHub Compare Links - GitHub Gist
2. Comparing commits - GitHub Docs
3. How To Compare Branches on GitHub? - GeeksforGeeks

---

### User
I think I did the tagging wrong. I did this:
1. created 3 dirs one for each stage
2. completed version 1
3. tagged version 1
4. copied version 1 into version 2 dir
5. did version 2 changes there.

I can't use traditional tag compares. should I just submit the sha compares for the stage comparisons?

---

### Gemini
Yes—if you have already committed your changes across those separate directories, submitting SHA compare links (or creating new tags on the specific commits for stages 2 and 3) is the fastest way to get working links right now without reorganising your repository.

#### Why Traditional Tag Compares Aren't Working
In Git, tags point to specific commits in a single timeline. Because you copied your files into separate folders (`v1/`, `v2/`, `v3/`) rather than editing the same files in place:
* Git treats `v2/app.js` as a brand-new file rather than a modified version of `v1/app.js`.
* A standard compare link will show 100% new additions in the new directory rather than a clean line-by-line diff of what changed.

#### Option 1: Use SHA Compare Links (Fastest)
If your repository already has commits for each stage, you can compare the commit hashes directly:

`https://github.com/OWNER/REPO/compare/SHA_STAGE_1...SHA_STAGE_2`

* **To get the SHAs:** Go to your repository on GitHub, click **Commits**, and click the copy icon next to the commit hash for the end of each stage.
* **To tag those commits instead:** If your submission requires actual tags (like `v2.0`), you can still attach tags to those specific commits without moving any files:
  ```bash
  git tag v2 <SHA_OF_STAGE_2_COMMIT>
  git push origin v2
  ```
  Once pushed, `.../compare/v1...v2` will work normally.

#### Option 2: Fix the Structure for Clean Line-by-Line Diffs (Optional)
If whoever is grading or reviewing your submission expects a line-by-line diff (green/red highlights showing exact edits rather than whole new files), you can quickly rebuild a clean linear history on a new branch:

1. Create a fresh branch from your Stage 1 commit:
   ```bash
   git checkout -b clean-history <SHA_OF_STAGE_1>
   ```
2. Move the files from your Stage 1 folder into the root of the repository, commit, and tag it `stage-1`.
3. Copy the contents of your Stage 2 folder over those root files (overwriting them), delete the extra folders, commit, and tag it `stage-2`.
4. Repeat for Stage 3, tag it `stage-3`, and push everything with `git push origin clean-history --tags`.

This gives you clean `.../compare/stage-1...stage-2` links where every modified line is properly highlighted.