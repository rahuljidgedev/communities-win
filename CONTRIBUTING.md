# Contributing to Community Win 

Welome to the team and **thank you** for your interest.

This contributing guide is specifically about contributing to the project. Their are many ways you can contribute to Jellow.

# Table of Contents

* [Code of Conduct](#code-of-conduct)
* [Contributing](#contributing)
* [Submitting Code Changes](#submitting-code-changes)
    - [Git Workflow](#git-workflow)
    - [Issues](#issues)
    - [Style (Java)](#style-java)
    - [Pull Requests](#pull-requests)
## Code of Conduct

Help us keep Community Win team welcoming. Please read and abide by the [Code of Conduct][coc].


## Submitting Code Changes
If you're new to contributing to open source on GitHub, this next section should help you get started.
If you get stuck, open an issue to ask us for help and we'll get you sorted out (and improve these instructions)[discussions][].


### Git Workflow

1. Fork and clone.
2. Add the upstream Jellow repository as a new remote to your clone.
   `git remote add upstream https://github.com/rahuljidgedev/community-win.git`
3. Create a new branch (with name of functionality )
   `git checkout -b name-of-branch`
   example: `git checkout -b grocery-management`
4. Commit and push as usual on your branch.
5. After you pushed the code. Discuss, review and ensured it will work correctly.
5. When you're ready, submit a pull request to staging branch. If everything is intact then we'll 
   merge it on master branch.

### Issues

We keep track of bugs, enhancements and support requests in the repository using GitHub [issues][].

### Style (Java)

We use [AOSP Java Code Style for Contributors][coding-style] 
to enforce a few style-related conventions.

### Pull Requests

When submitting a pull request, sometimes we'll ask you to make changes before
we accept the patch.

Please do not close the first pull request and open a second one with these
changes. If you push more commits to a branch that you've opened a pull
request for, it automatically updates the pull request.

As with adding more commits, you do not need to close your pull request and open a new one.
If you change the history (`rebase`, `squash`, `amend`), use `git push --force` to update the branch on your fork.
The pull request points to that branch, not to specific commits.

Here's a guide on [how to squash commits in a GitHub pull request][squash-commits].

### Deployment

After your pull request is merged, the functionality will get added to next release.

[coc]: https://github.com/rahuljidgedev/community-win/blob/master/CODE_OF_CONDUCT.md
[coding-style]: https://source.android.com/setup/contribute/code-style
[squash-commits]: http://blog.steveklabnik.com/posts/2012-11-08-how-to-squash-commits-in-a-github-pull-request
[issues]: https://github.com/rahuljidgedev/community-win/issues
[discussions]: https://github.com/rahuljidgedev/community-win/issues
