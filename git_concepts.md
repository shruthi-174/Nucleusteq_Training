GIT
1) Version control system: all the tools and techniques used to manage the software that we build. It is used for tracking and managing the changes to software code. Types: Localized, Centralized, Distributed.
2)Localized: It is a local database, stored on your local computer. Dis: If any system crash occurs to local database, data get lost.
3)Centralized: In this, repository in stored in central server. Developers work on code locally and the changes are committed directly in central repository.  Dis: single point of failure.
4)Distributed: Git, is one of the examples of distributed control system. In this, developers have a local copy of the entire repository. Changes can be made locally and committed without affecting the central repository. It provides more flexibility.
5)Git: It is a free and open-source version control system. It helps you to keep track and manage your source code history.
6)Git snapshot: a state of the project or code at a given moment or time.
7)Git States: there are 5 stages in git, which are:
•	Untracked: Files that are not yet tracked by git, these are newly created files which git is not aware of.
•	Unmodified: Files are tracked by git, and have not been changed since the last commit. 
•	Modified: Files are tracked by git, and have modified since the last commit.
•	Staged: Modified files are marked to be included in the next commit, done using git add cmd.
•	Committed: Files that have been saved in the git repository with a commit.
8)Git Commands: 
1.	Git init: initializes a new git repository in directory
2.	Git status: display the current state of repository
3.	Git add . / Git add filename: add changes in the working directory to the staging area
4.	Git commit -m “message”: save the changes of the staging area to the local repository
5.	Git log: display commit history in the reverse chronological order
6.	Revert Changes: 
a.	Git restore <filename>: this command will restore the file to its state in the last commit.
b.	Git restore <filename> --staged <file>: used to unstage the changes that have been added to the staging area but not yet committed.
c.	Git reset: reset the current state of the repository to a specific point.
i.	Git reset –soft HEAD~1:  it moves the HEAD pointer and the branch pointer to a specific commit, keeping all the changes of the last commit in the working directory.
ii.	Git reset –hard HEAD~1: it moves the HEAD and the branch pointer to a specific commit, discarding any changes in both the repository and working directory.

7.	Git branches: a pointer that points to a snapshot. When developer wants to add a different feature without affecting the main branch.
i.	Git branch branch_name: to create a new branch
ii.	Git checkout branch_name: switching to a new branch
iii.	Git branch -d branch_name: to delete a branch
8.	Git Stash: it is used to temporarily save the changes that you haven’t committed yet.
9.	Git merge Branch_name: combine the changes from one branch to another.
10.	Merge conflicts: when two branches are accessing or working with the same file, so conflicts occur, which can be resolved by manually selecting which data to keep.
11.	Git fetch name: downloads the latest changes from the remote repository to a local repository.
12.	Git pull: it is the combination of git fetch and git merge. Downloads the changes ad combines them to current repository.
13.	Git fork: creating a copy of a repository in your account, and then freely making the changes without affecting the original repository.
14.	Git push: uploads the local repository content to a remote repository.
15.	Git rebase: integrate changes from one branch to another branch, without unnecessary merge commits. 
16.	Git diff: used to show the changes between commits, branches, etc.
  
 

