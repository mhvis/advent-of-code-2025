import unittest
import day4


class MyTestCase(unittest.TestCase):
    def test_parse_input(self):
        input = [
            ".@@.\n",
            "@..@\n",
            "....\n",
            "\n",
        ]
        self.assertEqual(
            [
                [False, True, True, False],
                [True, False, False, True],
                [False, False, False, False],
            ],
            day4.parse_input(input),
        )


if __name__ == "__main__":
    unittest.main()
