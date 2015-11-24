./gradlew run || exit
cp -r site build
cd build/site
rm .gitignore
git init
git config user.name "Travis build"
git config user.email "tlorrain@excilys.com"
git add .
git commit -m "Deploy last h2geo version"
# Force push from the current repo's master branch to the remote
# repo's gh-pages branch. (All previous history on the gh-pages branch
# will be lost, since we are overwriting it.) We redirect any output to
# /dev/null to hide any sensitive credential data that might otherwise be exposed.
git push --force --quiet "https://${GH_TOKEN}@${GH_REF}" master:gh-pages > /dev/null 2>&1

