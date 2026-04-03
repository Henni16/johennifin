# Custom Release Update Guide

This guide details the exact steps required to pull a new official Jellyfin release and apply our custom deviations to generate a new custom build.

## Prerequisites
Ensure your working directory is clean and all previous work is safely committed or stashed.

## Step 1: Fetch the Latest Changes
Pull all the newest changes from the official upstream repository.
```bash
git fetch upstream
```

## Step 2: Create a new branch based on the new official release
Let's say the new official release is `v0.19.8` and your current custom branch is `custom-release-0.19.7`.

```bash
git checkout -b custom-release-0.19.8 upstream/release-0.19.8
```

## Step 3: Apply your custom changes onto the new branch
The best way to do this is using rebase if your changes are a clean set of commits:
```bash
git rebase --onto custom-release-0.19.8 release-0.19.7 custom-release-0.19.7
```
Note: This command takes the commits that are on custom-release-0.19.7 but NOT on release-0.19.7, and applies them on top of your new branch.

## Step 4: Resolving Rebase Conflicts
If the official developers updated the same files we modified, Git will pause the rebase procedure and alert you of a merge conflict.
1. Open the conflicting files in your editor and manually resolve the `<<<<<<<` and `>>>>>>>` blocks.
2. Cross-reference the `CUSTOM_DEVIATIONS.md` tracker to remind yourself exactly what our logic does so it is preserved efficiently.
3. Once you've fixed the file, stage it and tell the rebase to continue:
```bash
git add <conflicted-file>
git rebase --continue
```
*(Repeat this until the rebase finishes applying all your custom commits).*


## Step 5: Test the Build
Compile and test the build to ensure the customizations still function perfectly with the newly integrated official code.
```bash
./gradlew assembleRelease
```

## Step 6: Push the New Branch
Finally, securely back up your new custom release branch to your personal remote fork (`origin`).
```bash
git push -u origin custom-release-0.19.8
```
