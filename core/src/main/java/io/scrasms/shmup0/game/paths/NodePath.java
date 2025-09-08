package io.scrasms.shmup0.game.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.scrasms.shmup0.game.Path;
import io.scrasms.shmup0.game.Vector2Pair;

public class NodePath implements Path {
    private Array<Path> segments;
    private Array<Float> indexLengths;

    public NodePath(Array<Vector2Pair> pairs) {
        segments = new Array<>();
        indexLengths = new Array<>();
        Array<Float> lengths = new Array<>();
        for (int i = 0; i < pairs.size - 1; i++) {
            straightSegment straight = new straightSegment(pairs.get(i));
            segments.add(straight);
            lengths.add(straight.getLength());
            Vector2Pair next = pairs.get(i + 1);
            arcSegment arc = new arcSegment(pairs.get(i), next);
            segments.add(arc);
            lengths.add(arc.getLength());
            next.setFirst(arc.pathPos(arc.getLength()));
        }
        segments.add(new straightSegment(pairs.get(pairs.size - 1)));

        System.out.println(segments.size);
        System.out.println(lengths);

        indexLengths = new Array<>();
        indexLengths.add(0f);
        for (int i = 0; i < lengths.size; i++) {
            indexLengths.add(indexLengths.get(i) + lengths.get(i));
        }

        System.out.println(indexLengths);
    }

    @Override
    public Vector2 pathPos(float distance) {
        int i = indexLengths.size - 1;
        while (distance < indexLengths.get(i)) i--;
        return segments.get(i).pathPos(distance - indexLengths.get(i));
    }

    public float getTotalLength() {
        return indexLengths.get(indexLengths.size - 1);
    }

    private class straightSegment implements Path {
        private Vector2 start;
        private Vector2 direction;
        private float length;
        public straightSegment(Vector2Pair segment) {
            start = segment.first();
            length = new Vector2(segment.second()).sub(start).len();
            direction = new Vector2(segment.second()).sub(start).nor();

            System.out.println("Straight Segment: ");
            System.out.println(start);
            System.out.println(segment.second());
            System.out.println(length);
            System.out.println(direction);
            System.out.println("===");
        }

        @Override
        public Vector2 pathPos(float distance) {
            return new Vector2(start).add(new Vector2(direction).scl(distance));
        }

        public float getLength() {
            return length;
        }
    }

    private class arcSegment implements Path {
        private float radius;
        private Vector2 center;
        private float startAngle;
        private float angleTaken;
        private float length;

        public arcSegment(Vector2Pair start, Vector2Pair end) {
            // Incoming segment BA to outgoing segment XY
            Vector2 incoming = new Vector2(start.first()).sub(start.second());
            // YX vector
            Vector2 outgoing = new Vector2(end.second()).sub(end.first());
            // XA vector
            Vector2 connection = new Vector2(end.first()).sub(start.second());
            // YA vector
            Vector2 zag = new Vector2(start.second()).sub(end.second());
            // Angle at intersection
            float alpha = incoming.angleRad(outgoing);
            radius = connection.len() * (float)Math.sin(outgoing.angleRad(connection))/(1+(float)Math.cos(alpha));
            // Center at C
            center = new Vector2(start.second()).add(new Vector2(incoming).nor().rotate90(-1).scl(radius));
            // Angle of CA vector
            float zagAngle = outgoing.angleRad() - zag.angleRad();
            if (zagAngle > Math.PI) zagAngle -= (float)Math.PI;
            System.out.print("Zag Angle: ");
            System.out.println(zagAngle);
            if (zagAngle == 0) length = 0;
            else {
                if (radius < 0 && zagAngle <= 0) {
                    angleTaken = (float)Math.PI + alpha;
                    startAngle = new Vector2(start.second()).sub(center).angleRad() + (float)Math.PI;
                    System.out.println("A");
                }
                else if (radius > 0 && zagAngle <= 0) {
                    angleTaken = (float)Math.PI - alpha;
                    startAngle = new Vector2(start.second()).sub(center).angleRad();
                    System.out.println("B");
                }
                else if (radius > 0 && zagAngle < Math.PI) {
                    angleTaken = alpha - (float)Math.PI;
                    startAngle = new Vector2(start.second()).sub(center).angleRad();
                    System.out.println("C");
                }
                else if (radius < 0 && zagAngle < Math.PI) {
                    angleTaken = (float)Math.PI + alpha;
                    startAngle = new Vector2(start.second()).sub(center).angleRad() + (float)Math.PI;
                    System.out.println("D");
                }
                length = Math.abs(angleTaken * radius);
            }

            System.out.print("Radius: ");
            System.out.println(radius);
            System.out.print("Center: ");
            System.out.println(center);
            System.out.print("Max Angle: ");
            System.out.println(angleTaken);
            System.out.print("Start Angle: ");
            System.out.println(startAngle);
            System.out.println("===");
        }

        @Override
        public Vector2 pathPos(float distance) {
            float angle = (distance/radius) + startAngle;
            return new Vector2(radius, 0).rotateRad(angle).add(center);
        }

        public float getLength() {
            return length;
        }
    }
}
