#git submodule deinit -f ag-ui
#git rm -f ag-ui
#rm -rf .git/modules/ag-ui
#git status
#git commit -m "build: remove ag-ui submodule"
#git add .gitmodules
#git commit -m "build: remove ag-ui submodule config"

git submodule deinit -f ag-ui
git rm -f ag-ui
rm -rf .git/modules/ag-ui

git submodule add https://github.com/ag-ui-protocol/ag-ui.git ag-ui
git add .gitmodules ag-ui
git commit -m "build: re-add ag-ui submodule"