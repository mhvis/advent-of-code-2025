import unittest
import day5
from day5 import Range


class MyTestCase(unittest.TestCase):
    def test_remove_overlap_inner(self):
        self.assertEqual(
            [Range(2, 6), Range(1, 1)],
            day5.remove_overlap(
                [
                    Range(2, 6),
                    Range(2, 4),
                    Range(2, 3),
                    Range(1, 4),
                    Range(1, 3),
                ]
            ),
        )

    def test_remove_overlap2(self):
        self.assertEqual(
            [Range(1, 3)],
            day5.remove_overlap(
                [
                    Range(2, 2),
                    Range(1, 3),
                ]
            ),
        )

    def test_remove_overlap3(self):
        self.assertEqual(
            [Range(1, 3)],
            day5.remove_overlap(
                [
                    Range(1, 3),
                    Range(2, 2),
                ]
            ),
        )


if __name__ == "__main__":
    unittest.main()
