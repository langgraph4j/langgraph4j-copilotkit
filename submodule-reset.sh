rm -rf ag-ui
git submodule deinit -f ag-ui
rm -rf .git/modules/ag-ui

git submodule add https://github.com/ag-ui-protocol/ag-ui.git ag-ui
cd ag-ui
#git checkout main
#cd ..
#
#git add .gitmodules ag-ui
#git commit -m "fix: reset ag-ui submodule reference"
#git push