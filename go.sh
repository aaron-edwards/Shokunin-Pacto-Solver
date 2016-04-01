if [ "$1" == "test" ];
then
  lein test
  exit "$?"
fi

if [ "$1" == "run" ];
then
  PORT=4567 lein run https://api.github.com/repos/software-shokunin/coffee-api-challenge/contents/contracts
  exit "$?"
fi

echo "Usage"
echo "./go.sh test|run"
